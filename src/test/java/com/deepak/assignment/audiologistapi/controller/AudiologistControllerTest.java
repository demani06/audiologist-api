package com.deepak.assignment.audiologistapi.controller;

import com.deepak.assignment.audiologistapi.domain.Audiologist;
import com.deepak.assignment.audiologistapi.domain.Customer;
import com.deepak.assignment.audiologistapi.domain.CustomerAppointment;
import com.deepak.assignment.audiologistapi.domain.CustomerAppointmentDTO;
import com.deepak.assignment.audiologistapi.service.AudiologistService;
import com.deepak.assignment.audiologistapi.service.CustomerAppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(AudiologistController.class)
public class AudiologistControllerTest {

    @Mock
    AudiologistService audiologistService;
    @Mock
    CustomerAppointmentService customerAppointmentService;
    @Autowired
    private MockMvc mvc;
    @InjectMocks
    private AudiologistController audiologistController;

    private JacksonTester<CustomerAppointment> customerAppointmentRequest;
    private JacksonTester<CustomerAppointmentDTO> customerAppointmentDTOJacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mvc = MockMvcBuilders.standaloneSetup(audiologistController)
                .build();

        Customer testCustomer = Customer.builder().customerId(1).firstName("Subhasish").lastName("Roy").phoneNumber(988888).build();
        Audiologist testAudiologist = Audiologist.builder().audiologistId(1).firstName("John").lastName("Dean").build();
        CustomerAppointmentDTO testCustomerAppointmentDTO = CustomerAppointmentDTO.
                builder().
                customer(testCustomer).
                audiologist(testAudiologist).
                appointmentTimeStamp(LocalDateTime.now().minusDays(1)).
                build();

        //Given
        given(audiologistService.findCustomerById(1))
                .willReturn(Optional.ofNullable(testCustomer));

        given(audiologistService.findAudiologistById(1))
                .willReturn(Optional.ofNullable(testAudiologist));

        //Given
        given(customerAppointmentService.getNextAppointmentForCustomer(1))
                .willReturn(Optional.ofNullable(testCustomerAppointmentDTO));

    /*    given(customerAppointmentService.saveAppointment(testCustomerAppointmentDTO))
                .willReturn(testCustomerAppointmentDTO);*/
    }

    @Test
    public void testGetAppointments() throws Exception {

        //When
        MockHttpServletResponse response = mvc.perform(post("/api/audiologist/appointment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        customerAppointmentRequest.write(
                                new CustomerAppointment(1, 1, 1, "2018-12-12T12:35", 3, "")).getJson()
                ))
                .andReturn()
                .getResponse();

        //then
        System.out.println("response=" + response.getContentAsString());
        System.out.println("response=" + response.getStatus());
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());

    }


    @Test
    public void testGetAppointmentsForCustomerNotFound() throws Exception {

        //When
        MockHttpServletResponse response = mvc.perform(post("/api/audiologist/appointment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        customerAppointmentRequest.write(
                                new CustomerAppointment(1, 2, 1, "2018-12-12T12:35", 3, "")).getJson()
                ))
                .andReturn()
                .getResponse();

        //then
        System.out.println("response=" + response.getContentAsString());
        System.out.println("response=" + response.getStatus());
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());

    }


    @Test
    public void testGetAppointmentsForAudiologistNotFound() throws Exception {

        //When
        MockHttpServletResponse response = mvc.perform(post("/api/audiologist/appointment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        customerAppointmentRequest.write(
                                new CustomerAppointment(1, 1, 2, "2018-12-12T12:35", 3, "")).getJson()
                ))
                .andReturn()
                .getResponse();

        //then
        System.out.println("response=" + response.getContentAsString());
        System.out.println("response=" + response.getStatus());
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());

    }


    @Test
    public void testGetAppointmentsForDateFormatIssue() throws Exception {
        //When
        MockHttpServletResponse response = mvc.perform(post("/api/audiologist/appointment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        customerAppointmentRequest.write(
                                new CustomerAppointment(1, 1, 1, "2018-22-22T12:35", 3, "")).getJson()
                ))
                .andReturn()
                .getResponse();

        //then
        System.out.println("response=" + response.getContentAsString());
        System.out.println("response=" + response.getStatus());
        assertEquals(response.getStatus(), HttpStatus.UNPROCESSABLE_ENTITY.value());

    }

    @Test
    public void TestGetNextAppointmentForCustomer() throws Exception {
        //When
        MockHttpServletResponse response = mvc.perform(get("/api/customer/1/appointment").
                accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        //then
        System.out.println("response=" + response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());

        Customer testCustomer = Customer.builder().customerId(1).firstName("Subhasish").lastName("Roy").phoneNumber(988888).build();
        Audiologist testAudiologist = Audiologist.builder().audiologistId(1).firstName("John").lastName("Dean").build();
        CustomerAppointmentDTO testCustomerAppointmentDTO = CustomerAppointmentDTO.
                builder().
                customer(testCustomer).
                audiologist(testAudiologist).
                appointmentTimeStamp(LocalDateTime.now().minusDays(1)).
                build();

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JSR310Module());
        objectMapper.setDateFormat(new ISO8601DateFormat());
        final String expectedJson = objectMapper.writeValueAsString(testCustomerAppointmentDTO);

        assertEquals(response.getContentAsString(), expectedJson);

    }


}
