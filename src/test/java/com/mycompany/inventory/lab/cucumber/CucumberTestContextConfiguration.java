package com.mycompany.inventory.lab.cucumber;

import com.mycompany.inventory.lab.InventoryLabApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = InventoryLabApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
