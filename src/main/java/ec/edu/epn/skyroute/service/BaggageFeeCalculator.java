package ec.edu.epn.skyroute.service;

import org.springframework.stereotype.Service;

/**
 * Calcula las tarifas de equipaje para la aerolínea SkyRoute Airlines.
 * <p>
 * Reglas de negocio:
 * <ol>
 * <li>Tarifa base: $30.0 por maleta.</li>
 * <li>Exceso de peso: +$50.0 si una maleta pesa más de 23 kg.</li>
 * <li>Beneficio VIP: primera maleta gratis si el pasajero es VIP
 * y la maleta no excede 23 kg.</li>
 * <li>Excepciones: weight ≤ 0, bagCount < 1, o passengerId nulo
 * lanzan IllegalArgumentException.</li>
 * </ol>
 */
@Service
public class BaggageFeeCalculator {

    private final PassengerService passengerService;

    public BaggageFeeCalculator(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    /**
     * Calcula la tarifa total de equipaje.
     *
     * @param weight      peso de cada maleta (kg)
     * @param bagCount    cantidad de maletas
     * @param passengerId identificador del pasajero
     * @return costo total en dólares
     * @throws IllegalArgumentException si los parámetros no cumplen las
     *                                  restricciones
     */
    public double calculateFee(double weight, int bagCount, Long passengerId) {
        // Restricciones de Integridad
        if (weight <= 0 || bagCount < 1 || passengerId == null) {
            throw new IllegalArgumentException("Parámetros de equipaje inválidos");
        }

        // Definimos las variables
        final double precioBase = 30.0;
        final double recargoUnico = 50.0;
        boolean vipPassenger = passengerService.isVip(passengerId);
        double totalCosto = 0.0;

        // Lógica de negocio
        for (int i = 0; i < bagCount; i++) {
            boolean esPrimeraMalteta = i == 0;
            boolean descuentoVip = vipPassenger && esPrimeraMalteta && weight <= 23;

            if (descuentoVip) {
                totalCosto += 0.0;
            } else {
                totalCosto += precioBase;
                if (weight > 23) {
                    totalCosto += recargoUnico;
                }
            }
        }

        return totalCosto;
    }
}
