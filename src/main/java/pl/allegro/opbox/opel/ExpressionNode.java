package pl.allegro.opbox.opel;

import java.util.concurrent.CompletableFuture;

interface ExpressionNode {
    CompletableFuture<?> getValue(EvalContext context);

    default CompletableFuture<?> getValue() {
        return getValue(EvalContext.empty());
    }
}
