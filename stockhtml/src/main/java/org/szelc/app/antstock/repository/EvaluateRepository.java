package org.szelc.app.antstock.repository;

import org.szelc.app.antstock.data.evaluate.Evaluate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author szelc.org
 */
public class EvaluateRepository {

    private final List<Evaluate> evaluateStockDataList = new ArrayList();

    public void addData(Evaluate data) {
        evaluateStockDataList.add(data);
    }

    public void clearData() {
        evaluateStockDataList.clear();
    }

    public List<Evaluate> getEvaluateStockDataList() {
        return evaluateStockDataList;
    }

    public Evaluate getEvaluateData(String company) {
        Optional<Evaluate> evaluateOptional = evaluateStockDataList.stream().filter(e -> e.getCompanyName().equals(company)).findAny();
        return evaluateOptional.isPresent() ? evaluateOptional.get() : null;
    }

    public List<String> getCompanies() {
        return evaluateStockDataList.stream().map(Evaluate::getCompanyName).collect(Collectors.toList());
    }

    public Set<String> getCompaniesSet() {
        return evaluateStockDataList.stream().map(Evaluate::getCompanyName).collect(Collectors.toSet());
    }

}
