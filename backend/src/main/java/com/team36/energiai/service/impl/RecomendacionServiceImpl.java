package com.team36.energiai.service.impl;

import com.team36.energiai.service.RecomendacionService;
import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.model.TipoInmueble;
import org.springframework.stereotype.Service;
import com.team36.energiai.model.Categoria;
import java.util.function.BiPredicate;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;


@Service
public class RecomendacionServiceImpl implements RecomendacionService {

    private record Rule(
            BiPredicate<AnalisisRequest, Categoria> condition,
            String text,
            int priority
    ) {
    }

    private final List<Rule> rules = List.of(

            // HORARIO PICO

            new Rule(
                    (request, categoria) -> Boolean.TRUE.equals(request.usoHorarioPico()),
                    "Evita usar tus equipos de mayor consumo durante el horario pico. Puede reducir tu factura entre un 10% y 20%.",
                    1
            ),

            // CANTIDAD DE EQUIPOS

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() <= 8,
                    "Con pocos equipos, un mantenimiento preventivo anual basta para mantenerlos eficientes.",
                    4
            ),

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() > 8 &&
                            request.cantidadEquipos() <= 15,
                    "Programa revisiones semestrales de tus equipos: el desgaste acumulado puede elevar tu consumo hasta un 15%.",
                    3
            ),

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() > 15,
                    "Desconecta los equipos que no estés usando activamente; el consumo en espera puede llegar al 10% de tu factura.",
                    2
            ),

            // HORAS DE ALTO CONSUMO

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() <= 3,
                    "Buen manejo de tus horas de alto consumo. Sigue concentrándolas en franjas cortas.",
                    5
            ),

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() > 3 &&
                            request.horasAltoConsumo() <= 6,
                    "Distribuye tus actividades de mayor consumo en distintos momentos del día para evitar picos de demanda.",
                    3
            ),

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() > 6,
                    "Divide tus actividades de alto consumo en bloques cortos; el uso prolongado eleva significativamente el costo.",
                    1
            ),

            // CATEGORÍA + TIPO DE INMUEBLE

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.CASA,
                    "Mejora el aislamiento de tu vivienda y revisa el estado de tus equipos de climatización.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.CASA,
                    "Mantén revisiones periódicas de tus instalaciones eléctricas.",
                    5
            ),

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.DEPARTAMENTO,
                    "Evita usar varios equipos de alta potencia al mismo tiempo; en espacios reducidos elevan mucho la demanda.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.DEPARTAMENTO,
                    "Mantén escalonado el uso de tus equipos.",
                    5
            ),

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.LOCAL,
                    "Implementa un plan de gestión energética con auditorías trimestrales y metas mensuales de reducción.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.LOCAL,
                    "Monitorea el consumo mensual y documenta variaciones.",
                    5
            )
    );

    @Override
    public List<String> generar(AnalisisRequest request, Categoria categoria) {

        // Obtener las reglas que aplican al usuario
        List<Rule> reglasAplicadas = rules.stream()
                .filter(rule -> rule.condition().test(request, categoria))
                .sorted(Comparator.comparingInt(Rule::priority))
                .toList();

        // Extraer únicamente los textos de las recomendaciones
        List<String> recomendaciones = new ArrayList<>(
                reglasAplicadas.stream()
                        .map(Rule::text)
                        .distinct()
                        .limit(5)
                        .toList()
        );

        // Garantizar al menos dos recomendaciones
        if (recomendaciones.size() < 2) {
            completarMinimoRecomendaciones(recomendaciones, categoria);
        }

        return recomendaciones;
    }

    private void completarMinimoRecomendaciones(List<String> recomendaciones, Categoria categoria) {
        switch (categoria) {
            case EFICIENTE -> {
                agregarSiHaceFalta(recomendaciones, "Continúa monitoreando periódicamente tu consumo para mantener un uso eficiente de la energía.");
                agregarSiHaceFalta(recomendaciones, "Realiza mantenimiento preventivo a tus equipos para conservar su eficiencia.");
            }
            case MODERADO -> {
                agregarSiHaceFalta(recomendaciones, "Identifica oportunidades de ahorro revisando el consumo de tus equipos eléctricos.");
                agregarSiHaceFalta(recomendaciones, "Evita el uso innecesario de equipos durante las horas de mayor demanda energética.");
            }
            case INEFICIENTE -> {
                agregarSiHaceFalta(recomendaciones, "Prioriza la sustitución de equipos con alto consumo energético.");
                agregarSiHaceFalta(recomendaciones, "Implementa un plan de ahorro energético y monitorea los resultados periódicamente.");
            }
        }
    }

    private void agregarSiHaceFalta(List<String> recomendaciones, String texto) {
        if (recomendaciones.size() >= 2) {
            return;
        }
        if (!recomendaciones.contains(texto)) {
            recomendaciones.add(texto);
        }
    }
}