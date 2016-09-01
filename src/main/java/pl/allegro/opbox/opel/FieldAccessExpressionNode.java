package pl.allegro.opbox.opel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class FieldAccessExpressionNode implements ExpressionNode {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ExpressionNode subject;
    private final ExpressionNode fieldName;

    public FieldAccessExpressionNode(ExpressionNode subject, ExpressionNode fieldName) {
        this.subject = subject;
        this.fieldName = fieldName;
    }

    @Override
    public CompletableFuture<?> getValue(EvalContext context) {
        return extractValueFromMap(subject.getValue(context), fieldName.getValue(context));
    }

    private CompletableFuture<?> extractValueFromMap(CompletableFuture<?> obj, CompletableFuture<?> key) {
        return obj.thenCombine(key, (it, k) -> {
            if (it == null) {
                logger.info("Can't extract value for key '" + k + "' from null");
                return null;
            }
            if (it instanceof Map) {
                return ((Map) it).get(k);
            }
            throw new OpelException("Give me a map, given " + it.getClass().getSimpleName());
        });
    }
}
