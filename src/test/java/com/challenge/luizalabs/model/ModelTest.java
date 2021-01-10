package com.challenge.luizalabs.model;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.SerializableMustHaveSerialVersionUIDRule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.pojo.tester.api.assertion.Method;

@RunWith(MockitoJUnitRunner.class)
public class ModelTest {

  protected List<PojoClass> pojoClasses = new ArrayList<PojoClass>();

  @Before
  public void setUp() {
    this.pojoClasses.add(PojoClassFactory.getPojoClass(Customer.class));
    this.pojoClasses.add(PojoClassFactory.getPojoClass(Product.class));
    this.pojoClasses.add(PojoClassFactory.getPojoClass(WishList.class));
  }

  @Test
  public void annotationEntity() {
    pojoClasses.forEach(clazz ->
      Stream.of(clazz.getClazz().getAnnotations())
          .filter(annotation -> annotation instanceof Entity)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("class does not entity annotation")));
  }

  @Test
  public void annotationTable() {
    pojoClasses.forEach(clazz ->
      Stream.of(clazz.getClazz().getAnnotations())
          .filter(annotation -> annotation instanceof Table)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("class does not table annotation")));
  }

  @Test
  public void noArgsConstructor() {
    pojoClasses.forEach(clazz ->
      Stream.of(clazz.getClazz().getConstructors())
          .filter(constructor -> constructor.getParameterTypes().length == 0)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("class does not NoArgsContructor")));
  }

  @Test
  public void allArgsConstructor() {
    pojoClasses.forEach(clazz ->
      Stream.of(clazz.getClazz().getConstructors())
          .filter(constructor -> constructor.getParameterTypes().length > 0)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("class does not AllArgsContructor")));
  }

  @Test
  public void builder() {
    pojoClasses.forEach(clazz ->
      Stream.of(clazz.getClazz().getDeclaredClasses())
          .filter(c -> c.getName().contains("Builder"))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("class does not builder")));
  }

  @Test
  public void serialVersionUid() {
    pojoClasses.forEach(clazz ->
      ValidatorBuilder.create()
          .with(new SerializableMustHaveSerialVersionUIDRule())
          .build()
          .validate(clazz));
  }

  @Test
  public void checkNecessaryMethods() {
    pojoClasses.forEach(clazz ->
      assertPojoMethodsFor(clazz.getClazz())
          .testing(Method.GETTER)
          .testing(Method.SETTER)
          .testing(Method.CONSTRUCTOR)
          .testing(Method.TO_STRING)
          .testing(Method.EQUALS)
          .testing(Method.HASH_CODE)
          .areWellImplemented());
  }
}

