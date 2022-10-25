package miniStringBuilderApp.controller;




import miniStringBuilderApp.MiniStringBuilderAppApplication;
import miniStringBuilderApp.model.StringInput;
import miniStringBuilderApp.service.StringBuilderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.http.HttpServletResponse;

import static java.lang.System.lineSeparator;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= MiniStringBuilderAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class StringBuilderControllerTest {



    @InjectMocks
    public StringBuilderController stringBuilderController;

    @Mock
    public StringBuilderService stringBuilderService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup () {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();

    }


    @Test
    @DisplayName("Ensure that StringInput would be returned after save to database")
    public void it_should_return_created_stringInput() throws Exception {

        StringInput stringInput = new StringInput(1, 7,"aabc", BigInteger.valueOf(7L));

//        Mockito.when(stringBuilderService.createFileFromString(Mockito.anyString(),
//                Mockito.any(StringInput.class))).thenReturn(stringInput);

        mockMvc.perform(post("/api/v1/string-input")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +"\"minLength\": 1,\n" + "\"maxLength\": 7,\n" + "\"userInput\": \"aabc\",\n" + "\"numberOfUniqueStringsRequested\":7\n" + " }"))
                    .andExpect(status().is(201))
                    .andExpect(MockMvcResultMatchers.content().string(containsString("\"userInput\":\"aabc\"")));


    }

    @Test
    @DisplayName("Ensure that get all jobs request will return list of jobs")
    public void testGetAllStringPermutationJobs() {

        StringInput stringInput1 = new StringInput(1, 16,"aabc", BigInteger.valueOf(7L));
        StringInput stringInput2 = new StringInput(1, 4,"asAD12UD", BigInteger.valueOf(16L));
        ArrayList<StringInput> stringInputs = new ArrayList<>();
        stringInputs.add(stringInput1);
        stringInputs.add(stringInput2);


        when(stringBuilderService.getAllStringInputFile()).thenReturn(stringInputs);

        // when
        ResponseEntity<List> result = stringBuilderController.getAllStringPermutationJobs();

        // then
        assertThat(result.getBody().size()).isEqualTo(2);

        StringInput stringInputTest1 = (StringInput) result.getBody().get(0);
        assertThat(stringInputTest1.getUserInput())
                .isEqualTo(stringInput1.getUserInput());

        StringInput stringInputTest2 = (StringInput) result.getBody().get(1);
        assertThat(stringInputTest2.getUserInput())
                .isEqualTo(stringInput2.getUserInput());


    }

    @Test
    public void when_getFile_should_return_file_name() throws Exception {

        mockMvc.perform(post("/api/v1/string-input")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +"\"minLength\": 1,\n" + "\"maxLength\": 7,\n" + "\"userInput\": \"aabc\",\n" + "\"numberOfUniqueStringsRequested\":7\n" + " }"));

       mockMvc.perform(get("/api/v1/files/1"))
                .andExpect(status().isOk());
               // .andExpect(MockMvcResultMatchers.content().string(containsString("a" + lineSeparator() +"ab" + lineSeparator()+ "abc" + lineSeparator()+ "ac" + lineSeparator()+"acb"+ lineSeparator()+ "ba"+ lineSeparator()+ "bac")));



    }
}