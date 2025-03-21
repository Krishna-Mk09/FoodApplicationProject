package com.order.jdp.orderservice.serviceimpl;

import com.order.jdp.orderservice.entity.FOODSequences;
import com.order.jdp.orderservice.repository.SequenceRepository;
import com.order.jdp.orderservice.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.relational.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class SequenceServiceImpl  implements SequenceService {

    @Autowired
    private SequenceRepository sequenceRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public long getSequenceByCustomer(String tableName,  long userId)
            throws Exception {
        log.info("trying to generate sequence id value from repository");
        BigDecimal sequence = BigDecimal.ZERO;
        long newSequence;
        String sequenceId = "";
        FOODSequences ajSequence = null;
        BigDecimal incrementValue = BigDecimal.ZERO;
        BigDecimal currentValue = BigDecimal.ZERO;
        BigDecimal maxValue = BigDecimal.valueOf(-1);
        BigDecimal cycle = BigDecimal.ZERO;
        synchronized (this) {
            List<FOODSequences> ajSequenceList = sequenceRepository.findByTableName(tableName.toUpperCase());



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
            if (!(maxValue.compareTo(BigDecimal.valueOf(-1)) == 0) && sequence.compareTo(maxValue) > 0
                    && cycle.compareTo(BigDecimal.ONE) == 0) {
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
