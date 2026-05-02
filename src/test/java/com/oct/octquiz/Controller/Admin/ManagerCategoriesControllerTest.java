package com.oct.octquiz.Controller.Admin;

import com.oct.octquiz.Model.Categoria.CategoriaEntity;
import com.oct.octquiz.Model.Categoria.CategoryService;
import com.oct.octquiz.Model.Categoria.Stat.StatCategoriaService;
import com.oct.octquiz.Model.Domanda.DomandaEntity;
import com.oct.octquiz.Model.Domanda.DomandaService;
import com.oct.octquiz.Model.User.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerCategoriesController.class)
public class ManagerCategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private DomandaService domandaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private StatCategoriaService statCategoriaService;

    private CategoriaEntity testCategory;
    private DomandaEntity testQuestion;

    @BeforeEach
    void setUp() {
        testCategory = new CategoriaEntity(1, "Test Category", 30, true);
        testQuestion = new DomandaEntity(1, testCategory, "Question 1", null, null, "A1", "A2", "A3", "A4", 1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditQuestion() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findByCategoriaAndId(testCategory, 1)).thenReturn(testQuestion);

        mockMvc.perform(multipart("/admin/edit-question")
                        .param("id_categoria", "1")
                        .param("id_domanda", "1")
                        .param("domanda", "Updated Question")
                        .param("risposta1", "R1")
                        .param("risposta2", "R2")
                        .param("risposta3", "R3")
                        .param("risposta4", "R4")
                        .param("rispostaCorretta", "2")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(domandaService).update(any(DomandaEntity.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEditTextQuestion() throws Exception {
        when(categoryService.findById(1)).thenReturn(testCategory);
        when(domandaService.findByCategoriaAndId(testCategory, 1)).thenReturn(testQuestion);

        mockMvc.perform(post("/admin/edit-text-question")
                        .param("id_categoria", "1")
                        .param("id_domanda", "1")
                        .param("testo", "Updated Text Question")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(domandaService).update(any(DomandaEntity.class));
    }
}
