package com.welb.medicalEthics.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluation")
public class MedicalEthicsEvaluationController {
    private final Logger log = LogManager.getLogger(this.getClass());
}
