CREATE TABLE RUBRICA_INSS (
  NU_NB varchar(15) primary key,              
  ID_OL_CONCESSAO varchar(15),    
  ID_OL_MANUTENCAO varchar(15),   
  ID_OL_MANUT_ANT varchar(15),    
  CS_PA varchar(8),               
  VL_MR_ATU decimal(19,2),        
  VL_RMI decimal(19,2),           
  CS_TRATAMENTO varchar(5),       
  CS_ESPECIE varchar(5),          
  CS_RAMO_ATIVIDADE varchar(5),   
  CS_FORMA_FILIACAO varchar(5),   
  CS_DOC_EMPREGADOR varchar(5),   
  NU_DOC_EMPREGADOR varchar(14),  
  NU_NB_ANT varchar(10),          
  D2_DER varchar(8),              
  D2_DIB varchar(8),              
  D2_DDB varchar(8),              
  D2_DCB varchar(8),              
  D2_DIP varchar(8),              
  D2_INI_INCAPAC varchar(8),      
  D2_INICIO_DOENCA varchar(8),    
  D2_DRD varchar(8),              
  D2_OBITO_RECLUSAO varchar(8),   
  CS_CLIENTELA varchar(1),        
  NU_MATR_CONCESSOR varchar(8),   
  NU_MATR_HABILITADOR varchar(8), 
  CS_SITUACAO_BENEF varchar(5),   
  ID_BANCO varchar(3),            
  ID_ORGAO_PAGADOR varchar(8),    
  CS_MEIO_PAGTO varchar(5),       
  NU_AGENCIA_PAG varchar(6),      
  NU_CONTA_CORRENTE varchar(10),  
  CS_DIAGNOSTICO_N varchar(30),    
  CS_DIAGNOSTICO_1 varchar(30),    
  NU_MATR_MP1 varchar(15),        
  NU_MATR_MP2 varchar(15),        
  D2_FORMAT_CONC varchar(8),      
  CS_DESPACHO varchar(8),         
  DT_DIA_UTIL_PAGTO varchar(8),   
  NM_RECEBEDOR varchar(100),      
  DN_RECEBEDOR varchar(8),        
  NU_CPF_R varchar(11),           
  CS_SEXO_R varchar(1),           
  NM_TITULAR_BENEF_T varchar(100),
  NM_MAE_T varchar(100),          
  NU_CPF_T varchar(11),           
  ID_NIT_T varchar(11),           
  DT_NASCIMENTO_T varchar(8),     
  CTPS_T varchar(7),              
  CTPS_SERIE_T varchar(5),        
  CTPS_UF_T varchar(2),           
  NU_IDENTIDADE_T varchar(11),    
  IDENTIDADE_UF_T varchar(2),     
  CS_EMISSOR_T varchar(2),        
  NU_TIT_ELEITOR varchar(13),     
  CS_VAL_CNIS varchar(2),         
  CS_SEXO_T varchar(2),           
  TE_ENDERECO_T varchar(100),     
  NM_BAIRRO_T varchar(50),        
  NU_CEP_T varchar(8),            
  NU_DDD_T varchar(2),            
  NU_TELEFONE_T varchar(11),      
  ID_MUN_SINPAS_T varchar(5),     
  ID_MUN_IBGE_T varchar(6),       
  NM_MUNICIPIO_T varchar(100),    
  NM_UF_MUNICIPIO_T varchar(2),   
  D2_OBITO_T varchar(8),          
  NM_INSTITUIDOR_I varchar(100),  
  NM_MAE_I varchar(100),          
  NU_CPF_I varchar(11),           
  ID_NIT_I varchar(11),           
  DT_NASCIMENTO_I varchar(8),     
  CTPS_I varchar(7),              
  CTPS_SERIE_I varchar(5),        
  CTPS_UF_I varchar(2),           
  NU_IDENTIDADE_I varchar(11),    
  IDENTIDADE_UF_I varchar(2),     
  CS_EMISSOR_I varchar(2),        
  NU_TIT_ELEITOR_I varchar(15),   
  CS_VAL_CNIS_I varchar(1),       
  CS_SEXO_I varchar(1),           
  D2_OBITO_I varchar(8),          
  NM_PROCURADOR_P varchar(100),   
  NM_MAE_P varchar(100),          
  NU_CPF_P varchar(11),           
  ID_NIT_P varchar(11),           
  DT_NASCIMENTO_P varchar(8),     
  CTPS_P varchar(7),              
  CTPS_SERIE_P varchar(5),        
  CTPS_UF_P varchar(2),           
  NU_IDENTIDADE_P varchar(11),    
  IDENTIDADE_UF_P varchar(2),     
  CS_EMISSOR_P varchar(2),        
  CS_SEXO_P varchar(2),           
  NM_BAIRRO_P varchar(50),        
  NU_CEP_P varchar(8),            
  TE_ENDERECO_P varchar(8),       
  NM_MUNICIPIO_P varchar(50),     
  NM_UF_MUNICIPIO_P varchar(2),   
  MUNICIP_NASC_P varchar(50),     
  NM_REPRESENTANTE_R varchar(50), 
  NM_MAE_R varchar(50),           
  ID_NIT_R varchar(11),           
  DT_NASCIMENTO_R varchar(8),     
  CTPS_R varchar(7),              
  CTPS_SERIE_R varchar(5),        
  CTPS_UF_R varchar(2),           
  NU_IDENTIDADE_R varchar(15),    
  IDENTIDADE_UF_R varchar(2),     
  CS_EMISSOR_R varchar(2),        
  CS_TIPO_R varchar(1),           
  QT_DEP_IR int,                  
  QT_DEP_VAL_NB int,              
  QT_DEP_CADASTRO int,            
  QT_RUBRICA_REG int,             
  CS_RUBRICA_1 int,               
  CS_RUBRICA_2 int,               
  CS_RUBRICA_3 int,               
  CS_RUBRICA_4 int,               
  CS_RUBRICA_5 int,               
  CS_RUBRICA_6 int,               
  CS_RUBRICA_7 int,               
  CS_RUBRICA_8 int,               
  CS_RUBRICA_9 int,               
  CS_RUBRICA_10 int,              
  VL_RUBRICA_1 decimal(19,2),     
  VL_RUBRICA_2 decimal(19,2),     
  VL_RUBRICA_3 decimal(19,2),     
  VL_RUBRICA_4 decimal(19,2),     
  VL_RUBRICA_5 decimal(19,2),     
  VL_RUBRICA_6 decimal(19,2),     
  VL_RUBRICA_7 decimal(19,2),     
  VL_RUBRICA_8 decimal(19,2),     
  VL_RUBRICA_9 decimal(19,2),     
  VL_RUBRICA_10 decimal(19,2),    
  VL_BRUTO decimal(19,2),         
  TOT_DESCONTOS decimal(19,2),    
  VL_LIQUIDO decimal(19,2),       
  NU_CPF varchar(11),             
  CS_SEXO varchar(1),             
  DT_ULTIMA_ALTER  varchar(8),    
  D2_LIMITE varchar(8),           
  DS_ERRO varchar(50),            
  DT_ATUALIZACAO_ETL  varchar(30),
  NM_ARQUIVO varchar(50),         
  ANO_MES_REF varchar(6),         
  DT_ULTIMA_PERICIA  varchar(8),  
  FASE_ULTIMA_PERICIA varchar(2)  
);





















































































































































