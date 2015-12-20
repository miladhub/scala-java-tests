package scalajava;

import static org.junit.Assert.*;

import javax.script.Compilable;
import javax.script.CompiledScript;

import org.junit.Before;
import org.junit.Test;

import scala.tools.nsc.Settings;
import scala.tools.nsc.interpreter.*;

public class ScalaTest {
	private IMain engine;

	@Before
	public void asd() {
		Settings settings = new Settings();
		settings.usejavacp().tryToSetFromPropertyValue("true");
		engine = new IMain(settings);
	}
	
	@Test
	public void foo() throws Exception {
		engine.eval("println(\"foo\")");
	}
	
	@Test
	public void bar() throws Exception {
		engine.eval("println(\"bar\")");
	}
	
	@Test
	public void interpreted() throws Exception {	
		engine.put("n: Int", 10);
		for (int i = 0; i < 3; i++) {
			Object ret = engine.eval("import scalajava._\n" + "def asd(n: Int) : Int = if (n == 0) 42 else {\n"
					+ "println(\"recursing...\")\n" + "asd(n-1) } \n" + "println(\"hello! \" + new MyJavaObj)\n"
					+ "asd(3)");
			assertEquals(42, ret);
		}
	}
	
	@Test
	public void compiled() throws Exception {
		Compilable c = engine;
		CompiledScript script = c.compile("import scalajava._\n" +
				"def asd(n: Int) : Int = if (n == 0) 42 else {\n" +
				"println(\"recursing...\")\n" +
				"asd(n-1) } \n" +
				"println(\"hello! \" + new MyJavaObj)\n" +
				"asd(3)");
		
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 3; i++) {
			assertEquals(42, script.eval());
		}
		System.out.println("compiled took " + (System.currentTimeMillis() - t1) + "ms");
	}
}
