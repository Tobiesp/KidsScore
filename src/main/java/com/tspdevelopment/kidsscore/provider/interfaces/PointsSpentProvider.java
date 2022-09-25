package com.tspdevelopment.kidsscore.provider.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tspdevelopment.kidsscore.data.model.PointsSpent;

public interface PointsSpentProvider extends BaseProvider<PointsSpent>{

    public PointsSpent create(PointsSpent newItem);

    public void delete(UUID id);

    public List<PointsSpent> findAll();

    public Optional<PointsSpent> findById(UUID id);

    public PointsSpent update(PointsSpent replaceItem, UUID id);

    public List<PointsSpent> search(PointsSpent item);

    public List<PointsSpent> findByEventDate(Date eventDate);
    
    public List<PointsSpent> searchEventDate(Date start, Date end);
    
}
