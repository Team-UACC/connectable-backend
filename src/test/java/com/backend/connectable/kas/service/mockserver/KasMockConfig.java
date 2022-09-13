package com.backend.connectable.kas.service.mockserver;

import com.backend.connectable.kas.config.KasWebClient;
import com.backend.connectable.kas.service.KasService;
import com.backend.connectable.kas.service.common.endpoint.EndPointGenerator;
import com.backend.connectable.kas.service.contract.KasContractService;
import com.backend.connectable.kas.service.contract.TransactionOptionManager;
import com.backend.connectable.kas.service.token.KasTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class KasMockConfig {

    @Autowired private KasWebClient kasWebClient;

    @Autowired private TransactionOptionManager transactionOptionManager;

    @Bean
    public KasService kasMockService() {
        return new KasService(kasMockContractService(), kasMockTokenService());
    }

    @Bean
    public KasContractService kasMockContractService() {
        return new KasContractService(
                kasWebClient, kasMockEndPointGenerator(), transactionOptionManager);
    }

    @Bean
    public KasTokenService kasMockTokenService() {
        return new KasTokenService(kasWebClient, kasMockEndPointGenerator());
    }

    @Bean
    @Primary
    public EndPointGenerator kasMockEndPointGenerator() {
        return new KasMockEndPointGenerator();
    }
}
