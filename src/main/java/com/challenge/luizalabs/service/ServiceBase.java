package com.challenge.luizalabs.service;

import java.util.List;

public interface ServiceBase<Q, S> {

  S saveOrUpdate(Q dto, Long id);

  List<S> getAll();

  S getById(Long id);

  void delete(Long id);
}
