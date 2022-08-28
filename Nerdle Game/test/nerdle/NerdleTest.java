/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package nerdle;

import java.awt.Color;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author eren_
 */
public class NerdleTest {
	Nerdle obje = new Nerdle();
	
        //Dondurulen denklemin sol tarafi, sag tarafi saglamali.
        @Test
        public void returnedEquationShouldBeTrue(){
            assertTrue(obje.isEquationTrue(obje.generateEquation()));
        }
        
        //Fonksiyon null dondurmemeli.
	@Test
	public void functionShouldNotReturnNull() {
            assertNotNull(obje.generateEquation());
	}
	
        //Dondurulen denklemin uzunlugu 7 ile 9 arasinda olmali.
	@Test 
	public void returnedEquationLengthShouldBeBetweenSevenAndNine(){
            int equationLength;
            equationLength = obje.generateEquation().length();
            assertTrue(equationLength < 10 || equationLength > 6);
	}
	
        //Dondurulen denklemde sadece 1 adet esittir isareti olmali.
	@Test
	public void returnedEquationShouldContainExactlyOneEqualSymbol() {
            String equation = obje.generateEquation();
            assertTrue(equation.length() - equation.replace("=", "").length() == 1);
	}
	
        //Dondurulen denklem bir veya daha fazla operator icermeli.
	@Test
	public void returnedEquationShouldContainOneOrMoreOperator() {
            String equation = obje.generateEquation();
            int operatorCount;
            operatorCount = 4 * equation.length() - equation.replace("+", "").length() - equation.replace("-", "").length()
                    - equation.replace("*", "").length() - equation.replace("/", "").length();
            assertTrue(operatorCount >= 1);
	}
        
        //Operatorler, dort islem isaretlerinden farkli olmamalidir.
        @Test
	public void generatedOperatorsShouldNotBeDifferentFromFourOperations() {
            String equation = obje.generateEquation();
            assertTrue(equation.contains("+") || equation.contains("-") || equation.contains("*") || equation.contains("/"));
	}
    
 
}
