/*
 * @author Ankit Varshney
 */

package com.delhitransit.core.service;

import com.delhitransit.core.EntityGenerator;
import com.delhitransit.core.model.entity.StopTimeEntity;
import com.delhitransit.core.repository.StopTimeRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class StopTimeServiceTest {

    private final StopTimeEntity stopTimeEntity = EntityGenerator.StopTimeEntityGenerator.generate();

    private StopTimeService stopTimeService;

    @BeforeEach
    void setup() {
        StopTimeRepository mockStopTImeRepository = mock(StopTimeRepository.class);
        StopTimeEntity stopTimeEntity = EntityGenerator.StopTimeEntityGenerator.generate();

    }

}

