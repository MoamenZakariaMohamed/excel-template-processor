/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.proccess.populator.service;

import com.example.proccess.upload.domain.ServiceType;
import com.example.proccess.upload.domain.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkImportWorkbookPopulatorServiceImpl implements BulkImportWorkbookPopulatorService {

    private static final Logger LOG = LoggerFactory.getLogger(BulkImportWorkbookPopulatorServiceImpl.class);

    private final ServiceTypeRepository serviceTypeRepository;



    @Override
    public byte[] getTemplate(String entityType, Long officeId) {
        final Workbook workbook = new HSSFWorkbook();
        WorkbookPopulator populator = populateUserWorkbook();
        populator.populate(workbook);
        return buildResponse(workbook);
    }


    private List<ServiceType> fetchServiceTypes() {
           return this.serviceTypeRepository.findAll();
    }

    private byte[] buildResponse(final Workbook workbook) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            LOG.error("Problem occurred in buildResponse function", e);
        }
       return  baos.toByteArray();
    }

    private WorkbookPopulator populateUserWorkbook() {
        List<ServiceType> serviceTypeList = fetchServiceTypes();
        return new ServicesWorkbookPopulator(new ServiceTypeSheetPopulator(serviceTypeList));
    }



}
