package com.tspdevelopment.kidsscore.provider.sqlprovider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;

import com.tspdevelopment.kidsscore.data.model.Group;
import com.tspdevelopment.kidsscore.data.repository.GroupRepository;
import com.tspdevelopment.kidsscore.provider.interfaces.GroupProvider;

public class GroupProviderImpl implements GroupProvider{

    private final GroupRepository repository;
    
    public GroupProviderImpl(GroupRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<Group> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Group> findById(UUID id) {
        return this.repository.findById(id);
    }

    @Override
    public Group create(Group newItem) {
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
    public Group update(Group replaceItem, UUID id) {
        if (replaceItem == null) {
            throw new IllegalArgumentException("Updated Student can not be null.");
        }
        return repository.findById(id) //
                .map(item -> {
                    item.setName(replaceItem.getName());
                    item.setModifiedAt(LocalDateTime.now());
                    return repository.save(item);
                }) //
                .orElseGet(() -> {
                    return repository.save(replaceItem);
                });
    }

    @Override
    public List<Group> search(Group item) {
        Example<Group> example = Example.of(item);
        return this.repository.findAll(example);
    }

    public Optional<Group> findByName(String name) {
        return this.repository.findByName(name);
    }

    public List<Group> findByNameLike(String name) {
        return this.repository.findByNameLike(name);
    }
    
}
