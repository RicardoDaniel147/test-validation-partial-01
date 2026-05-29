package ec.edu.epn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ec.edu.epn.skyroute.service.BaggageFeeCalculator;
import ec.edu.epn.skyroute.service.PassengerService;

@ExtendWith(MockitoExtension.class)
class BaggageFeeCalculatorTest {

    @Mock
    private PassengerService passengerService;

    private BaggageFeeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new BaggageFeeCalculator(passengerService);
    }

    @Test
    @DisplayName("Prueba 1: Cliente no vip que no excede el limite de peso en sus maletas (paga el precio base)")
    void calculoClienteNoVipSinExcesoDePeso() {
        when(passengerService.isVip(1728795459L)).thenReturn(false);

        double fee = calculator.calculateFee(20.0, 1, 1728795459L);

        assertEquals(30.0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba 2: Cliente no vip que excede el limite de peso en sus maletas (paga el recargo) ")
    void calculoClientNoVipConExcesoDePeso() {
        when(passengerService.isVip(1322L)).thenReturn(false);

        double fee = calculator.calculateFee(25.0, 1, 1322L);

        assertEquals(80.0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba 3: Cliente vip que no excede la cantidad de peso en sus maletas (Si una sola maleta, debe ser gratis)")
    void calculoClienteVipSinExcesoDePeso() {
        when(passengerService.isVip(123456L)).thenReturn(true);

        double fee = calculator.calculateFee(15.0, 1, 123456L);

        assertEquals(0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba 4: Caso limite Vip: 2 maletas, 15 kg cada una y siendo un pasajero vip")
    void calculoClienteVipSinExcesoDePeso2() {
        when(passengerService.isVip(11111L)).thenReturn(true);

        double fee = calculator.calculateFee(15.0, 2, 11111L);

        assertEquals(30.0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba 5: Cliente vip que excede la cantidad de peso en sus maletas (Paga un precio normal y recargo) ")
    void calculoClienteVipConExcesoDePeso() {
        when(passengerService.isVip(13L)).thenReturn(true);

        double fee = calculator.calculateFee(100.0, 2, 13L);

        assertEquals(160.0, fee, 0.0001);
    }

    @Test
    @DisplayName("Prueba 6: Maleta del cliente con peso cero")
    void rechazoCuandoMaletaTienePesoCero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateFee(0.0, 1, 14L));

        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
    }

    @Test
    @DisplayName("Prueba 7: Cliente nulo")
    void rechazoCuandoClienteEsNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateFee(20.0, 1, null));

        assertEquals("Parámetros de equipaje inválidos", exception.getMessage());
    }
}
