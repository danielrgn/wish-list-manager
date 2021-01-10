package com.challenge.luizalabs.commons;

import com.challenge.luizalabs.LuizalabsWlmApplication;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = {LuizalabsWlmApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public abstract class ContextGenericTest extends SchemaGenericTest {

}
