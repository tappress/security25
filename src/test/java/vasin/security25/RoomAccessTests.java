package vasin.security25;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class RoomAccessTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    // ============== PUBLIC ACCESS TESTS ==============

    @Test
    @WithAnonymousUser
    void whenAnonymousAccessPublicEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenAnonymousAccessProtectedEndpoint_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isUnauthorized());
    }

    // ============== AUTHENTICATED USER ACCESS TESTS ==============

    @Test
    @WithMockUser
    void whenAnyAuthenticatedUserAccessRooms_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenAnyAuthenticatedUserAccessHelloStranger_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/stranger"))
                .andExpect(status().isOk());
    }

    // ============== USER ROLE SPECIFIC ACCESS TESTS ==============

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserAccessUserEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserAccessAdminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserAccessSuperadminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/superadmin"))
                .andExpect(status().isForbidden());
    }

    // ============== ADMIN ROLE SPECIFIC ACCESS TESTS ==============

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminAccessAdminEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminAccessUserEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminAccessSuperadminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/superadmin"))
                .andExpect(status().isForbidden());
    }

    // ============== SUPERADMIN ROLE SPECIFIC ACCESS TESTS ==============

    @Test
    @WithMockUser(roles = {"SUPERADMIN"})
    void whenSuperadminAccessSuperadminEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/superadmin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"SUPERADMIN"})
    void whenSuperadminAccessUserEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"SUPERADMIN"})
    void whenSuperadminAccessAdminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/admin"))
                .andExpect(status().isForbidden());
    }

    // ============== SHARED ACCESS TESTS ==============

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserAccessSharedEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/unknown"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminAccessSharedEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/unknown"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"SUPERADMIN"})
    void whenSuperadminAccessSharedEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/rooms/hello/unknown"))
                .andExpect(status().isOk());
    }

    // ============== CRUD OPERATION TESTS ==============

    @Test
    @WithAnonymousUser
    void whenAnonymousCreateRoom_thenUnauthorized() throws Exception {
        String roomJson = "{ \"roomNumber\": \"601\", \"roomType\": \"Standard\", \"pricePerNight\": 75.99, \"capacity\": 2, \"available\": true, \"amenities\": \"WiFi, TV\", \"floorLevel\": \"6\" }";

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserCreateRoom_thenCreated() throws Exception {
        String roomJson = "{ \"roomNumber\": \"601\", \"roomType\": \"Standard\", \"pricePerNight\": 75.99, \"capacity\": 2, \"available\": true, \"amenities\": \"WiFi, TV\", \"floorLevel\": \"6\" }";

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminCreateRoom_thenCreated() throws Exception {
        String roomJson = "{ \"roomNumber\": \"701\", \"roomType\": \"Premium\", \"pricePerNight\": 175.99, \"capacity\": 3, \"available\": true, \"amenities\": \"WiFi, TV, AC, Mini Bar, Jacuzzi\", \"floorLevel\": \"7\" }";

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomJson))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    void whenAnonymousUpdateRoom_thenUnauthorized() throws Exception {
        String roomJson = "{ \"id\": \"1\", \"roomNumber\": \"102\", \"roomType\": \"Standard Plus\", \"pricePerNight\": 109.99, \"capacity\": 2, \"available\": true, \"amenities\": \"WiFi, TV, AC, Mini Fridge\", \"floorLevel\": \"1\" }";

        mockMvc.perform(put("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserUpdateRoom_thenOk() throws Exception {
        String roomJson = "{ \"id\": \"1\", \"roomNumber\": \"102\", \"roomType\": \"Standard Plus\", \"pricePerNight\": 109.99, \"capacity\": 2, \"available\": true, \"amenities\": \"WiFi, TV, AC, Mini Fridge\", \"floorLevel\": \"1\" }";

        mockMvc.perform(put("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roomJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenAnonymousDeleteRoom_thenUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/rooms/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserDeleteRoom_thenOk() throws Exception {
        mockMvc.perform(delete("/api/v1/rooms/1"))
                .andExpect(status().isOk());
    }
}