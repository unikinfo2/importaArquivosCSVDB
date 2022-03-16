package com.ImportaArquivosCSVDB.config;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.ImportaArquivosCSVDB.model.RubricaInss;


 
public class DBLogProcessor implements ItemProcessor<RubricaInss, RubricaInss>
{
	private static final Logger log = LoggerFactory.getLogger(DBLogProcessor.class);
    public RubricaInss process(RubricaInss rubricaInss) throws Exception
    {
        //System.out.println("Inserindo Rubrica Inss : " + rubricaInss);
        log.info("Inserindo Rubrica Inss : " + rubricaInss);
        return rubricaInss;
    }
}