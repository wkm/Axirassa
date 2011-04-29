
package zanoccio.experimental;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes({ "zanoccio.experimental.HasLength" })
public class LengthAP extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> arg0, RoundEnvironment arg1) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Kind.NOTE, "APF: " + arg0 + "\n" + arg1);

		return false;
	}

}
