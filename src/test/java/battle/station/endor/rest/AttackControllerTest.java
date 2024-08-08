package battle.station.endor.rest;

import battle.station.endor.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.time.Duration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AttackControllerTest {

    public static final String REQUEST_ATTACK = "{\n" +
            "\t\"protocols\": [\"avoid-mech\"],\n" +
            "\t\"scan\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"coordinates\": { \"x\": 0, \"y\": 40 },\n" +
            "\t\t\t\"enemies\": { \"type\": \"soldier\", \"number\": 10 }\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper mapper;

    public MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void init() {
        this.mockMvc = webAppContextSetup(webApplicationContext).alwaysDo(print()).build();
    }

    @Test
    void smoke() throws Exception {
        mockRestTemplate(1);
        attack(1);
    }

    private void mockRestTemplate(int gen) {
        doReturn(IonCannonStatusDto.builder().available(true).generation(gen).build()).when(restTemplate)
                .getForObject(any(URI.class), eq(IonCannonStatusDto.class));
        doReturn(FireIonCannonResponseDto.builder().generation(gen).casualties(10).build()).when(restTemplate)
                .postForObject(any(URI.class), any(FireIonCannonDto.class),
                        eq(FireIonCannonResponseDto.class));
    }

    @Test
    void sequenceGuns() throws Exception {
        mockRestTemplate(1);
        attack(1);
        Thread.sleep(Duration.ofSeconds(1));
        mockRestTemplate(2);
        attack(2);
        Thread.sleep(Duration.ofSeconds(1));
        mockRestTemplate(3);
        attack(3);
        Thread.sleep(Duration.ofSeconds(1));
        mockRestTemplate(2);
        attack(2);
    }

    private void attack(int gen) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/attack")
                        .content(REQUEST_ATTACK)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(mapper.writeValueAsString(AttackResponseDto.builder()
                        .generation(gen)
                        .casualties(10)
                        .target(CoordinatesProbeDto.builder().x(0).y(40).build())
                        .build())))
                .andExpect(status().isOk());
    }

}