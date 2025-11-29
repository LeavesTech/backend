package com.example.leavestech.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.leavestech.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CodingQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodingQuestionDTO.class);
        CodingQuestionDTO codingQuestionDTO1 = new CodingQuestionDTO();
        codingQuestionDTO1.setId(1L);
        CodingQuestionDTO codingQuestionDTO2 = new CodingQuestionDTO();
        assertThat(codingQuestionDTO1).isNotEqualTo(codingQuestionDTO2);
        codingQuestionDTO2.setId(codingQuestionDTO1.getId());
        assertThat(codingQuestionDTO1).isEqualTo(codingQuestionDTO2);
        codingQuestionDTO2.setId(2L);
        assertThat(codingQuestionDTO1).isNotEqualTo(codingQuestionDTO2);
        codingQuestionDTO1.setId(null);
        assertThat(codingQuestionDTO1).isNotEqualTo(codingQuestionDTO2);
    }
}
