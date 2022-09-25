package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.PointsEarned;
import com.tspdevelopment.kidsscore.data.repository.PointsEarnedRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.PointsEarnedProvider;

public class PointsEarnedProviderImpl implements PointsEarnedProvider{

    private final PointsEarnedRepository repository;
    
    public PointsEarnedProviderImpl(PointsEarnedRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<PointsEarned> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PointsEarned> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public PointsEarned create(PointsEarned newItem) {
        if((newItem != null) && (newItem.getCreatedAt() == null)) {
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setModifiedAt(LocalDateTime.now());
        }
        return this.repository.save(newItem);
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
        
    }

    @Override
    public PointsEarned update(PointsEarned replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setAttended(replaceItem.isAttended());
                    item.setAttentive(replaceItem.isAttentive());
                    item.setBible(replaceItem.isBible());
                    item.setBibleVerse(replaceItem.isBibleVerse());
                    item.setBringAFriend(replaceItem.isBringAFriend());
                    item.setEventDate(replaceItem.getEventDate());
                    item.setRecallsLastWeekLesson(replaceItem.isRecallsLastWeekLesson());
                    item.setStudent(replaceItem.getStudent());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<PointsEarned> search(PointsEarned item) {
        Example<PointsEarned> example = Example.of(item);
        return this.repository.findAll(example);
    }

    @Override
    public List<PointsEarned> findByAttended(Boolean attended) {
        return this.repository.findByAttended(attended);
    }

    @Override
    public List<PointsEarned> findByBible(Boolean bible) {
        return this.repository.findByBible(bible);
    }

    @Override
    public List<PointsEarned> findByBibleVerse(Boolean bibleVerse) {
        return this.repository.findByBibleVerse(bibleVerse);
    }

    @Override
    public List<PointsEarned> findByBringAFriend(Boolean bringAFriend) {
        return this.repository.findByBringAFriend(bringAFriend);
    }

    @Override
    public List<PointsEarned> findByAttentive(Boolean attentive) {
        return this.repository.findByAttentive(attentive);
    }

    @Override
    public List<PointsEarned> findByRecallsLastWeekLesson(Boolean recallsLastWeekLesson) {
        return this.repository.findByRecallsLastWeekLesson(recallsLastWeekLesson);
    }

    @Override
    public List<PointsEarned> findByEventDate(Date eventDate) {
        return this.repository.findByEventDate(eventDate);
    }

    @Override
    public List<PointsEarned> searchEventDate(Date start, Date end) {
        return this.repository.searchEventDate(start, end);
    }

    
    
}
