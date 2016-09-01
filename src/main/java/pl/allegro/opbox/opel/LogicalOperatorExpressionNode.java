package pl.allegro.opbox.opel;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

class LogicalOperatorExpressionNode extends BinaryOperationExpressionNode {

    private final Operator logicalOperator;
    private final ImplicitConversion conversion;

    private LogicalOperatorExpressionNode(Operator logicalOperator, ExpressionNode left, ExpressionNode right, ImplicitConversion implicitConversion) {
        super(left, right);
        this.logicalOperator = logicalOperator;
        this.conversion = implicitConversion;
    }

    static LogicalOperatorExpressionNode andOperator(ExpressionNode left, ExpressionNode right, ImplicitConversion implicitConversion) {
        return new pl.allegro.opbox.opel.LogicalOperatorExpressionNode(Operator.AND, left, right, implicitConversion);
    }

    static LogicalOperatorExpressionNode orOperator(ExpressionNode left, ExpressionNode right, ImplicitConversion implicitConversion) {
        return new pl.allegro.opbox.opel.LogicalOperatorExpressionNode(Operator.OR, left, right, implicitConversion);
    }

    @Override
    public CompletableFuture<Boolean> getValue(EvalContext context) {
        return left().getValue(context).thenCompose(left -> wrappingExceptionsWithOpelException(left, () -> {
            if (logicalOperator == Operator.OR && Boolean.TRUE.equals(conversion.convert(left, Boolean.class))) {
                return CompletableFuture.completedFuture(true);
            } else if (logicalOperator == Operator.AND && Boolean.FALSE.equals(conversion.convert(left, Boolean.class))) {
                return CompletableFuture.completedFuture(false);
            } else {
                return right().getValue(context).thenApply(right -> wrappingExceptionsWithOpelException(left, right,
                        () -> Boolean.TRUE.equals(conversion.convert(right, Boolean.class))));
            }
        }));
    }

    private <T> T wrappingExceptionsWithOpelException(Object left, Supplier<T> wrappedBody) {
        try {
            return wrappedBody.get();
        } catch (Exception e) {
            throw new OpelException("Error on evaluating left side of logical expression. " +
                    "operator: " + logicalOperator + ", " +
                    "left: " + left + ", class: " + left.getClass(), e);
        }
    }

    private <T> T wrappingExceptionsWithOpelException(Object left, Object right, Supplier<T> wrappedBody) {
        try {
            return wrappedBody.get();
        } catch (Exception e) {
            throw new OpelException("Error on evaluating logical expression. " +
                    "operator: " + logicalOperator + ", " +
                    "left: " + left + ", class: " + left.getClass() +
                    "right: " + right + ", class: " + right.getClass(), e);
        }
    }
}
