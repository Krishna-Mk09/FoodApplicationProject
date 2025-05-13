package com.foodapplication.jdp.Common_Service.ServiceImpl;

import com.foodapplication.jdp.Common_Service.Entity.Sequence;
import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.foodapplication.jdp.Common_Service.repository.SequenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

/*
 * Author Name : M.V.Krishna
 * Date: 25-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Slf4j
@Service
public class SequenceServiceImpl implements SequenceService {

    private final WebClient webClient;
    private final SequenceRepository sequenceRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public SequenceServiceImpl(SequenceRepository sequenceRepository, WebClient.Builder webClientBuilder) {
        this.sequenceRepository = sequenceRepository;
        this.webClient = webClientBuilder.baseUrl("http://user-authentication-service").build();
    }


//    public UserDTO getCurrentUser(String token) {
//        return webClient.get()
////                .uri("/userAuthService/userInfo")
//                .uri("/userAuthService/userInfo")
//                .header("Authorization",
//                        token)
//                .retrieve()
//                .bodyToMono(UserDTO.class)
//                .block(); // or use async if needed
//    }


    public UserDTO getCurrentUser(String token) {
        return webClientBuilder.baseUrl("http://user-authentication-service") // Eureka service name
                .build().get().uri("/userAuthService/userInfo").header("Authorization", token).retrieve().bodyToMono(UserDTO.class).block();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public long getSequenceByCustomer(String tableName) throws Exception {
        log.info("trying to generate sequence id value from repository");
        BigDecimal sequence = BigDecimal.ZERO;
        long newSequence;
        String sequenceId = "";
        Sequence ajSequence = null;
        BigDecimal incrementValue = BigDecimal.ZERO;
        BigDecimal currentValue = BigDecimal.ZERO;
        BigDecimal maxValue = BigDecimal.valueOf(-1);
        BigDecimal cycle = BigDecimal.ZERO;
        synchronized (this) {
            List<Sequence> ajSequenceList = sequenceRepository.findByTableName(tableName.toUpperCase());


            if (ajSequenceList != null && !ajSequenceList.isEmpty()) {

                ajSequence = ajSequenceList.get(0);
                incrementValue = ajSequence.getSeqIncrementVal();
                currentValue = ajSequence.getSeqCurrVal();
                maxValue = (ajSequence.getSeqMaxVal() != null ? ajSequence.getSeqMaxVal() : BigDecimal.valueOf(-1));

                cycle = ajSequence.getSeqCycle();

            }

            // increment the current value with the incremental value
            sequence = currentValue.add(incrementValue);

            // if the value is more than the maxValue of the sequence, restarting from start
            if (!(maxValue.compareTo(BigDecimal.valueOf(-1)) == 0) && sequence.compareTo(maxValue) > 0 && cycle.compareTo(BigDecimal.ONE) == 0) {
                // log.info("value is more than the maxValue - restarting from start");
                currentValue = currentValue.add(incrementValue);
                sequence = currentValue;
            }

            sequenceId = sequenceId + sequence.toPlainString();
            newSequence = new BigDecimal(sequenceId).longValue();


            // Updates AjSequence table with new sequence generated.
            if (ajSequence != null) {
                ajSequence.setSeqCurrVal(sequence);
                sequenceRepository.save(ajSequence);
            }
        }
        return newSequence;
    }


}
