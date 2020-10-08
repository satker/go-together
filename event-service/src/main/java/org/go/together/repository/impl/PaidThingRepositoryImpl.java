package org.go.together.repository.impl;

import org.go.together.model.PaidThing;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.PaidThingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaidThingRepositoryImpl extends CustomRepositoryImpl<PaidThing> implements PaidThingRepository {
}
