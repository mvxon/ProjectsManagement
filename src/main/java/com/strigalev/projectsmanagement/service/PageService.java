package com.strigalev.projectsmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageService<T> {
    Page<T> getPage(Pageable pageable);
}
