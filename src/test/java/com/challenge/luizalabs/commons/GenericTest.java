package com.challenge.luizalabs.commons;

import com.challenge.luizalabs.LuizalabsWlmApplication;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = {LuizalabsWlmApplication.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class GenericTest {
}
