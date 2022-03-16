package com.ImportaArquivosCSVDB.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.ImportaArquivosCSVDB.model.Hotels;
import com.ImportaArquivosCSVDB.model.RubricaInss;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("${nomebancodados}")
    private String nomebancodados;    
    
    private String arquivoProcessamento = "";

    @Bean
    public Job readCSVFileJob() {
        return jobBuilderFactory
                .get("readCSVFileJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }
    
    /**
     * StepBuilder which sets the JobRepository and PlatformTransactionManager automatically
     */

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("step")
                .<RubricaInss, RubricaInss>chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    
    /**
     * Prints the Logs in the console.
     * @return
     */

    @Bean
    public ItemProcessor<RubricaInss, RubricaInss> processor() {
        return new DBLogProcessor();
    }

    /**
     * FlatFileItemReader<T> Restartable ItemReader that reads lines from input setResource(Resource).
     * @return
     */
    
    @Bean
    public FlatFileItemReader<RubricaInss> reader() {
        log.info("Começando o processamento");
        FlatFileItemReader<RubricaInss> itemReader = new FlatFileItemReader<RubricaInss>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".csv");
            }
        };        
        File arquivos[];
        URL dir_url = ClassLoader.getSystemResource("/");
        log.info("Começando o processamento na pasta: "+dir_url);
        File diretorio = new File(dir_url.getPath());        //"/home/unik/Desenvolvimento/temp/arquivoslote"
        File diretorioDestino = new File(dir_url.getPath()); //"/home/unik/Desenvolvimento/temp/arquivoslote/processados"
        arquivos = diretorio.listFiles(filter);
        
        if(!diretorioDestino.exists()){
            //cria a pasta
        	diretorioDestino.mkdir();
        }        

        if(arquivos.length > 0){
       		
        	log.info("Arquivo sendo Processado: "+diretorio.getAbsolutePath()+"/"+arquivos[0].getName());
	        boolean ok = arquivos[0].renameTo(new File(diretorioDestino, arquivos[0].getName()));
	        if(ok){
        	   log.info("Arquivo foi movido com sucesso");
	        }
	        else{
        	   log.warn("Nao foi possivel mover o arquivo"); 
	        }           
	        itemReader.setResource(new ClassPathResource(diretorioDestino.getAbsolutePath()+"/"+arquivos[0].getName())); ///tmp/arquivoslote/Atualização RUBRICAS INSS 202112 (2).csv"
        }        
        return itemReader;
    }
    


    /**
		O sistema podera usar o MySql ou PostGreSQL
    */

    @Bean
    public DataSource dataSource() {

    	System.out.println("nomebancodados: "+nomebancodados);
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		if(nomebancodados.equals("postgresql")) {
			dataSource.setDriverClassName("org.postgresql.Driver");
			dataSource.setUrl("jdbc:postgresql://localhost:5432/avantdb");
			dataSource.setUsername("postgres");
			dataSource.setPassword("postgres");
		} else {
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://localhost:3306/unikinfo_dbneosunik");
			dataSource.setUsername("unik");
			dataSource.setPassword("unik3001");
		}
		
		//ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(); 
		//databasePopulator.addScript(new ClassPathResource("org/springframework/batch/core/schema-drop-"+nomebancodados+".sql"));
		//databasePopulator.addScript(new ClassPathResource("org/springframework/batch/core/schema-"+nomebancodados+".sql"));
		//DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    	
        return dataSource; 
    }

    /**
     * The itemWriter object will set JDBC connection and sql statement is prepared for the batch action we want to perform in the database.
     * A convenient implementation for providing BeanPropertySqlParameterSource when the item has JavaBean properties that correspond to names used for parameters in the SQL statement.
     *
     */

    @Bean
    public JdbcBatchItemWriter<RubricaInss> writer() {

        JdbcBatchItemWriter<RubricaInss> itemWriter = new JdbcBatchItemWriter<RubricaInss>();

        itemWriter.setDataSource(dataSource());
        itemWriter.setSql("INSERT INTO RUBRICA_INSS (NU_NB, ID_OL_CONCESSAO, ID_OL_MANUTENCAO, "+ 
                        		   "ID_OL_MANUT_ANT, CS_PA, VL_MR_ATU, VL_RMI, CS_TRATAMENTO, "+ 
                        		   "CS_ESPECIE, CS_RAMO_ATIVIDADE, CS_FORMA_FILIACAO, CS_DOC_EMPREGADOR, "+ 
                        		   "NU_DOC_EMPREGADOR, NU_NB_ANT, D2_DER, D2_DIB, D2_DDB, D2_DCB, "+
                        		   "D2_DIP, D2_INI_INCAPAC, D2_INICIO_DOENCA, D2_DRD, D2_OBITO_RECLUSAO, "+ 
                        		   "CS_CLIENTELA, NU_MATR_CONCESSOR, NU_MATR_HABILITADOR, CS_SITUACAO_BENEF, "+ 
                        		   "ID_BANCO, ID_ORGAO_PAGADOR, CS_MEIO_PAGTO, NU_AGENCIA_PAG, "+ 
                        		   "NU_CONTA_CORRENTE, CS_DIAGNOSTICO_N, CS_DIAGNOSTICO_1, NU_MATR_MP1, "+ 
                        		   "NU_MATR_MP2, D2_FORMAT_CONC, CS_DESPACHO, DT_DIA_UTIL_PAGTO, "+ 
                        		   "NM_RECEBEDOR, DN_RECEBEDOR, NU_CPF_R, CS_SEXO_R, NM_TITULAR_BENEF_T, "+ 
                        		   "NM_MAE_T, NU_CPF_T, ID_NIT_T, DT_NASCIMENTO_T, CTPS_T, CTPS_SERIE_T, "+ 
                        		   "CTPS_UF_T, NU_IDENTIDADE_T, IDENTIDADE_UF_T, CS_EMISSOR_T, "+ 
                        		   "NU_TIT_ELEITOR, CS_VAL_CNIS, CS_SEXO_T, TE_ENDERECO_T, NM_BAIRRO_T, "+ 
                        		   "NU_CEP_T, NU_DDD_T, NU_TELEFONE_T, ID_MUN_SINPAS_T, ID_MUN_IBGE_T, "+ 
                        		   "NM_MUNICIPIO_T, NM_UF_MUNICIPIO_T, D2_OBITO_T, NM_INSTITUIDOR_I, "+ 
                        		   "NM_MAE_I, NU_CPF_I, ID_NIT_I, DT_NASCIMENTO_I, CTPS_I, CTPS_SERIE_I, "+ 
                        		   "CTPS_UF_I, NU_IDENTIDADE_I, IDENTIDADE_UF_I, CS_EMISSOR_I, "+ 
                        		   "NU_TIT_ELEITOR_I, CS_VAL_CNIS_I, CS_SEXO_I, D2_OBITO_I, "+ 
                        		   "NM_PROCURADOR_P, NM_MAE_P, NU_CPF_P, ID_NIT_P, DT_NASCIMENTO_P, "+ 
                        		   "CTPS_P, CTPS_SERIE_P, CTPS_UF_P, NU_IDENTIDADE_P, IDENTIDADE_UF_P, "+ 
                        		   "CS_EMISSOR_P, CS_SEXO_P, NM_BAIRRO_P, NU_CEP_P, TE_ENDERECO_P, "+ 
                        		   "NM_MUNICIPIO_P, NM_UF_MUNICIPIO_P, MUNICIP_NASC_P, NM_REPRESENTANTE_R, "+ 
                        		   "NM_MAE_R, ID_NIT_R, DT_NASCIMENTO_R, CTPS_R, CTPS_SERIE_R, "+ 
                        		   "CTPS_UF_R, NU_IDENTIDADE_R, IDENTIDADE_UF_R, CS_EMISSOR_R, "+ 
                        		   "CS_TIPO_R, QT_DEP_IR, QT_DEP_VAL_NB, QT_DEP_CADASTRO, QT_RUBRICA_REG, "+ 
                        		   "CS_RUBRICA_1, CS_RUBRICA_2, CS_RUBRICA_3, CS_RUBRICA_4, CS_RUBRICA_5, "+ 
                        		   "CS_RUBRICA_6, CS_RUBRICA_7, CS_RUBRICA_8, CS_RUBRICA_9, CS_RUBRICA_10, "+ 
                        		   "VL_RUBRICA_1, VL_RUBRICA_2, VL_RUBRICA_3, VL_RUBRICA_4, VL_RUBRICA_5, "+ 
                        		   "VL_RUBRICA_6, VL_RUBRICA_7, VL_RUBRICA_8, VL_RUBRICA_9, VL_RUBRICA_10, "+ 
                        		   "VL_BRUTO, TOT_DESCONTOS, VL_LIQUIDO, NU_CPF, CS_SEXO, "+ 
                        		   "DT_ULTIMA_ALTER, D2_LIMITE, DS_ERRO, DT_ATUALIZACAO_ETL, "+ 
                        		   "NM_ARQUIVO, ANO_MES_REF, DT_ULTIMA_PERICIA, FASE_ULTIMA_PERICIA "+
                        		   ") VALUES ( "+
                          	     ":nuNb, :idOlConcessao, :idOlManutencao, :idOlManutAnt, "+ 
                        	     ":csPa, :vlMrAtu, :vlRmi, :csTratamento, "+ 
                        	     ":csEspecie, :csRamoAtividade, :csFormaFiliacao, :csDocEmpregador, "+ 
                        	     ":nuDocEmpregador, :nuNbAnt, :d2Der, :d2Dib, "+ 
                        	     ":d2Ddb, :d2Dcb, :d2Dip, :d2IniIncapac, "+ 
                        	     ":d2InicioDoenca, :d2Drd, :d2ObitoReclusao, :csClientela, "+ 
                        	     ":nuMatrConcessor, :nuMatrHabilitador, :csSituacaoBenef, :idBanco, "+ 
                        	     ":idOrgaoPagador, :csMeioPagto, :nuAgenciaPag, :nuContaCorrente, "+ 
                        	     ":csDiagnosticoN, :csDiagnostico1, :nuMatrMp1, :nuMatrMp2, "+ 
                        	     ":d2FormatConc, :csDespacho, :dtDiaUtilPagto, :nmRecebedor, "+ 
                        	     ":dnRecebedor, :nuCpfR, :csSexoR, :nmTitularBenefT, "+ 
                        	     ":nmMaeT, :nuCpfT, :idNitT, :dtNascimentoT, "+ 
                        	     ":ctpsT, :ctpsSerieT, :ctpsUfT, :nuIdentidadeT, "+ 
                        	     ":identidadeUfT, :csEmissorT, :nuTitEleitor, :csValCnis, "+ 
                        	     ":csSexoT, :teEnderecoT, :nmBairroT, :nuCepT, "+ 
                        	     ":nuDddT, :nuTelefoneT, :idMunSinpasT, :idMunIbgeT, "+ 
                        	     ":nmMunicipioT, :nmUfMunicipioT, :d2ObitoT, :nmInstituidorI, "+ 
                        	     ":nmMaeI, :nuCpfI, :idNitI, :dtNascimentoI, "+ 
                        	     ":ctpsI, :ctpsSerieI, :ctpsUfI, :nuIdentidadeI, "+ 
                        	     ":identidadeUfI, :csEmissorI, :nuTitEleitorI, :csValCnisI, "+ 
                        	     ":csSexoI, :d2ObitoI, :nmProcuradorP, :nmMaeP, "+ 
                        	     ":nuCpfP, :idNitP, :dtNascimentoP, :ctpsP, "+ 
                        	     ":ctpsSerieP, :ctpsUfP, "+ 
                        	     ":nuIdentidadeP, :identidadeUfP, :csEmissorP, :csSexoP, "+ 
                        	     ":nmBairroP, :nuCepP, :teEnderecoP, :nmMunicipioP, "+ 
                        	     ":nmUfMunicipioP, :municipNascP, :nmRepresentanteR, :nmMaeR, "+ 
                        	     ":idNitR, :dtNascimentoR, :ctpsR, :ctpsSerieR, "+ 
                        	     ":ctpsUfR, :nuIdentidadeR, :identidadeUfR, :csEmissorR, "+ 
                        	     ":csTipoR, :qtDepIr, :qtDepValNb, :qtDepCadastro, "+ 
                        	     ":qtRubricaReg, :csRubrica1, :csRubrica2, :csRubrica3, "+ 
                        	     ":csRubrica4, :csRubrica5, :csRubrica6, :csRubrica7, "+ 
                        	     ":csRubrica8, :csRubrica9, :csRubrica10, :vlRubrica1, "+ 
                        	     ":vlRubrica2, :vlRubrica3, :vlRubrica4, :vlRubrica5, "+ 
                        	     ":vlRubrica6, :vlRubrica7, :vlRubrica8, :vlRubrica9, "+ 
                        	     ":vlRubrica10, :vlBruto, :totDescontos, :vlLiquido, "+ 
                        	     ":nuCpf, :csSexo, :dtUltimaAlter, :d2Limite, "+ 
                        	     ":dsErro, :dtAtualizacaoEtl, :nmArquivo, :anoMesRef, "+ 
                        	     ":dtUltimaPericia, :faseUltimaPericia"+
        			")");                        				   
                        		   
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<RubricaInss>());
        return itemWriter;
    }

    /**
     * the lineMapper for mapping lines (strings) to domain objects typically used to map lines read from a file to domain objects on a per line basis.
     * lineTokenizer to split string obtained typically from a file into tokens. In our example we are using DelimitedLineTokenizer that is because we are using csv file.
     * fieldSetMapper to map data obtained from a FieldSet into an object.
     *
     */

    @Bean
    public LineMapper<RubricaInss> lineMapper() {
        DefaultLineMapper<RubricaInss> lineMapper = new DefaultLineMapper<RubricaInss>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        BeanWrapperFieldSetMapper<RubricaInss> fieldSetMapper = new BeanWrapperFieldSetMapper<RubricaInss>();

        lineTokenizer.setNames(new String[]{
        	     "nuNb", "idOlConcessao", "idOlManutencao", "idOlManutAnt", 
        	     "csPa", "vlMrAtu", "vlRmi", "csTratamento", 
        	     "csEspecie", "csRamoAtividade", "csFormaFiliacao", "csDocEmpregador", 
        	     "nuDocEmpregador", "nuNbAnt", "d2Der", "d2Dib", 
        	     "d2Ddb", "d2Dcb", "d2Dip", "d2IniIncapac", 
        	     "d2InicioDoenca", "d2Drd", "d2ObitoReclusao", "csClientela", 
        	     "nuMatrConcessor", "nuMatrHabilitador", "csSituacaoBenef", "idBanco", 
        	     "idOrgaoPagador", "csMeioPagto", "nuAgenciaPag", "nuContaCorrente", 
        	     "csDiagnosticoN", "csDiagnostico1", "nuMatrMp1", "nuMatrMp2", 
        	     "d2FormatConc", "csDespacho", "dtDiaUtilPagto", "nmRecebedor", 
        	     "dnRecebedor", "nuCpfR", "csSexoR", "nmTitularBenefT", 
        	     "nmMaeT", "nuCpfT", "idNitT", "dtNascimentoT", "ctpsT", 
        	     "ctpsSerieT", "ctpsUfT", "nuIdentidadeT", "identidadeUfT", 
        	     "csEmissorT", "nuTitEleitor", "csValCnis", "csSexoT", 
        	     "teEnderecoT", "nmBairroT", "nuCepT", "nuDddT", 
        	     "nuTelefoneT", "idMunSinpasT", "idMunIbgeT", "nmMunicipioT", 
        	     "nmUfMunicipioT", "d2ObitoT", "nmInstituidorI", "nmMaeI", 
        	     "nuCpfI", "idNitI", "dtNascimentoI", "ctpsI", 
        	     "ctpsSerieI", "ctpsUfI", "nuIdentidadeI", "identidadeUfI", 
        	     "csEmissorI", "nuTitEleitorI", "csValCnisI", "csSexoI", 
        	     "d2ObitoI", "nmProcuradorP", "nmMaeP", "nuCpfP", 
        	     "idNitP", "dtNascimentoP", "ctpsP", "ctpsSerieP", 
        	     "ctpsUfP", "nuIdentidadeP", "identidadeUfP", "csEmissorP", 
        	     "csSexoP", "nmBairroP", "nuCepP", "teEnderecoP", 
        	     "nmMunicipioP", "nmUfMunicipioP", "municipNascP", "nmRepresentanteR", 
        	     "nmMaeR", "idNitR", "dtNascimentoR", "ctpsR", 
        	     "ctpsSerieR", "ctpsUfR", "nuIdentidadeR", "identidadeUfR", 
        	     "csEmissorR", "csTipoR", "qtDepIr", "qtDepValNb", 
        	     "qtDepCadastro", "qtRubricaReg", "csRubrica1", "csRubrica2", 
        	     "csRubrica3", "csRubrica4", "csRubrica5", "csRubrica6", 
        	     "csRubrica7", "csRubrica8", "csRubrica9", "csRubrica10", 
        	     "vlRubrica1", "vlRubrica2", "vlRubrica3", "vlRubrica4", 
        	     "vlRubrica5", "vlRubrica6", "vlRubrica7", "vlRubrica8", 
        	     "vlRubrica9", "vlRubrica10", "vlBruto", "totDescontos", 
        	     "vlLiquido", "nuCpf", "csSexo", "dtUltimaAlter", 
        	     "d2Limite", "dsErro", "dtAtualizacaoEtl", "nmArquivo", 
        	     "anoMesRef", "dtUltimaPericia", "faseUltimaPericia"
        });
        
        lineTokenizer.setIncludedFields(new int[]{
										 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
										 10, 11, 12, 13, 14, 15,
										 16, 17, 18, 19, 20, 21, 22, 23,
										 24, 25, 26, 27, 28, 29, 30, 31,
										 32, 33, 34, 35, 36, 37, 38, 39,
										 40, 41, 42, 43, 44, 45, 46, 47,
										 48, 49, 50, 51, 52, 53, 54, 55,
										 56, 57, 58, 59, 60, 61, 62, 63,
										 64, 65, 66, 67, 68, 69, 70, 71,
										 72, 73, 74, 75, 76, 77, 78, 79,
										 80, 81, 82, 83, 84, 85, 86, 87,
										 88, 89, 90, 91, 92, 93, 94, 95,
										 96, 97, 98, 99, 
										 100, 101, 102, 103,
										 104, 105, 106, 107, 108, 109, 110, 111,
										 112, 113, 114, 115, 116, 117, 118, 119,
										 120, 121, 122, 123, 124, 125, 126, 127,
										 128, 129, 130, 131, 132, 133, 134, 135,
										 136, 137, 138, 139, 140, 141, 142, 143,
										 144, 145, 146, 147 });
        fieldSetMapper.setTargetType(RubricaInss.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


}
