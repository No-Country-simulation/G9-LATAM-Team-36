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
                    "Reduce el uso de equipos durante las horas pico para disminuir el costo del consumo eléctrico.",
                    1
            ),

            //CONSUMO

            new Rule(
                    (request, categoria) -> request.consumoKwh() <= 300,
                    "Mantén tus hábitos actuales de consumo y continúa monitoreando periódicamente el uso de energía.",
                    5
            ),

            new Rule(
                    (request, categoria) -> request.consumoKwh() > 300 && request.consumoKwh() <= 500,
                    "Evalúa los equipos con mayor consumo energético y optimiza su tiempo de uso.",
                    2
            ),

            new Rule(
                    (request, categoria) -> request.consumoKwh() > 500,
                    "Considera reemplazar equipos antiguos por modelos con mayor eficiencia energética.",
                    1
            ),

            //CANTIDAD DE EQUIPOS

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() <= 5,
                    "Realiza mantenimiento preventivo a tus equipos para conservar su eficiencia.",
                    4
            ),

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() > 5 &&
                            request.cantidadEquipos() <= 12,
                    "Programa revisiones periódicas para garantizar un consumo eficiente de todos los equipos.",
                    3
            ),

            new Rule(
                    (request, categoria) -> request.cantidadEquipos() > 12,
                    "Desconecta los equipos que permanezcan en modo de espera cuando no estén en uso.",
                    2
            ),

            //HORAS DE ALTO CONSUMO

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() <= 3,
                    "Continúa distribuyendo adecuadamente tus actividades de mayor consumo energético.",
                    5
            ),

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() > 3 &&
                            request.horasAltoConsumo() <= 10,
                    "Distribuye las actividades de mayor consumo durante diferentes momentos del día.",
                    2
            ),

            new Rule(
                    (request, categoria) -> request.horasAltoConsumo() > 10,
                    "Programa las actividades de alto consumo fuera de periodos prolongados para reducir la demanda energética.",
                    1
            ),

            // CATEGORÍA + TIPO DE INMUEBLE

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.CASA,
                    "Mejora el aislamiento térmico de la vivienda y optimiza el uso de los sistemas de climatización.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.CASA,
                    "Mantén el buen aislamiento de la vivienda y realiza revisiones periódicas de los equipos eléctricos.",
                    5
            ),

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.DEPARTAMENTO,
                    "Evita utilizar simultáneamente varios electrodomésticos de alta potencia para reducir la demanda energética.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.DEPARTAMENTO,
                    "Continúa utilizando de forma eficiente los electrodomésticos y evita consumos innecesarios.",
                    5
            ),

            new Rule(
                    (request, categoria) ->
                            categoria == Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.LOCAL,
                    "Implementa un plan de gestión energética para disminuir los costos operativos del establecimiento.",
                    2
            ),

            new Rule(
                    (request, categoria) ->
                            categoria != Categoria.INEFICIENTE &&
                                    request.tipoInmueble() == TipoInmueble.LOCAL,
                    "Monitorea periódicamente el consumo eléctrico del establecimiento para mantener una operación eficiente.",
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