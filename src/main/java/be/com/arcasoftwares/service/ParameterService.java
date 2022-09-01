package be.com.arcasoftwares.service;

import be.com.arcasoftwares.model.ParameterModel;
import be.com.arcasoftwares.model.ParameterModelDTO;
import be.com.arcasoftwares.model.ParameterProperty;
import be.com.arcasoftwares.repository.KorberRepository;
import be.com.arcasoftwares.repository.ParameterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ParameterService {

    private final KorberRepository<ParameterModel> parameterRepository;

    public ParameterService(final ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    public ParameterModel save(ParameterModel model) {
        return this.parameterRepository.upsert(model);
    }

    public List<ParameterModel> getAll() {
        return this.parameterRepository.findAll();
    }

    public List<ParameterModelDTO> getLatest() {
        List<ParameterModel> all = this.parameterRepository.findAll();
        Map<String, List<ParameterModel>> parametersGroupped = groupItemsByKey(all);
        List<ParameterModelDTO> parameterModelList = new ArrayList<>();
        parametersGroupped.forEach((k, v) -> {
            v.sort(Comparator.comparing(ParameterModel::getDate).reversed());
            ParameterModel model = v.get(0);
            parameterModelList.add(new ParameterModelDTO(model));
        });
        return parameterModelList;
    }

    public List<ParameterModel> getAllInTheLastXMinutes(int minutes) {
        List<ParameterModel> all = this.parameterRepository.findAll();

        if (minutes > 0) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MINUTE, -minutes);
            Date date = new Date();
            date.setTime(c.getTimeInMillis());
            all = all.stream().filter(item -> item.getDate().after(date)).collect(Collectors.toList());
        }
        return all;
    }

    public Map<String, Map<String, Map<String, Double>>> getSummary(int minutes) {
        List<ParameterModel> all = getAllInTheLastXMinutes(minutes);
        Map<String, List<ParameterModel>> parametersGroupped = groupItemsByKey(all);
        Map<String, Map<String, Map<String, Double>>> mapAll = new HashMap<>();
        parametersGroupped.forEach((machineKey, parameterModelList) -> {
            List<ParameterProperty> parameterPropertyList = accumulateAllParametersForMachine(parameterModelList);
            Map<String, List<ParameterProperty>> groupParametersByName = groupParametersByName(parameterPropertyList);

            Map<String, Map<String, Double>> map = calculateAvgMinMaxForProperty(groupParametersByName);

            mapAll.put(machineKey, map);
        });

        return mapAll;
    }

    private static Map<String, Map<String, Double>> calculateAvgMinMaxForProperty(final Map<String, List<ParameterProperty>> groupParametersByName) {
        Map<String, Map<String, Double>> map = new HashMap<>();
        for (Map.Entry<String, List<ParameterProperty>> parameter : groupParametersByName.entrySet()) {
            List<ParameterProperty> propValue = parameter.getValue();
            Double average = propValue.stream().mapToDouble(ParameterProperty::getValue).average().orElse(0);
            Double min = propValue.stream().mapToDouble(ParameterProperty::getValue).min().orElse(0);
            Double max = propValue.stream().mapToDouble(ParameterProperty::getValue).max().orElse(0);
            map.put(parameter.getKey(), Map.of("avg", average, "min", min, "max", max));
        }
        return map;
    }

    private Map<String, List<ParameterProperty>> groupParametersByName(final List<ParameterProperty> parameterPropertyList) {
        return parameterPropertyList.stream().collect(Collectors.groupingBy(ParameterProperty::getProperty));
    }

    private List<ParameterProperty> accumulateAllParametersForMachine(final List<ParameterModel> v) {
        List<ParameterProperty> parameterPropertyList = new ArrayList<>();
        v.forEach(item -> {
            List<ParameterProperty> parameters = item.getParameters();
            parameterPropertyList.addAll(parameters);

        });
        return parameterPropertyList;
    }

    private static Map<String, List<ParameterModel>> groupItemsByKey(final List<ParameterModel> all) {
        return all.stream().collect(Collectors.groupingBy(ParameterModel::getMachineKey));
    }

}
