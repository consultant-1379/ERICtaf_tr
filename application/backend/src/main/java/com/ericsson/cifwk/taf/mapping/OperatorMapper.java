package com.ericsson.cifwk.taf.mapping;

import com.ericsson.cifwk.scanner.model.PublicMethod;
import com.ericsson.cifwk.scanner.model.SharedOperator;
import com.ericsson.cifwk.taf.model.Operator;
import com.google.inject.Inject;

import java.util.List;
import java.util.UUID;

public class OperatorMapper {

    @Inject
    PublicMethodMapper publicMethodMapper;

    @Inject
    GavMapper gavMapper;

    public Operator map(SharedOperator sharedOperator) {
        Operator operatorEntity = new Operator();

        operatorEntity.setId(UUID.randomUUID().toString());
        operatorEntity.setName(sharedOperator.getName());
        operatorEntity.setGav(gavMapper.map(sharedOperator.getGav()));
        operatorEntity.addAllContext(sharedOperator.getContext());

        List<PublicMethod> publicMethods = sharedOperator.getPublicMethods();
        operatorEntity.setPublicMethodsCount(publicMethods.size());
        for (com.ericsson.cifwk.scanner.model.PublicMethod publicMethod : publicMethods) {
            operatorEntity.addPublicMethod(publicMethodMapper.map(publicMethod));
        }
        return operatorEntity;
    }

}
