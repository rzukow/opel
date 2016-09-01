package pl.allegro.opbox.opel;

class ExpressionNodeFactory {
    private final ImplicitConversion implicitConversion;
    private final MethodExecutionFilter methodExecutionFilter;

    public ExpressionNodeFactory(ImplicitConversion implicitConversion, MethodExecutionFilter methodExecutionFilter) {
        this.implicitConversion = implicitConversion;
        this.methodExecutionFilter = methodExecutionFilter;
    }

    public ExpressionNode binaryOperationNode(Operator operator, ExpressionNode left, ExpressionNode right) {
        return operator.createNode(left, right, implicitConversion);
    }

    public ExpressionNode ifNode(ExpressionNode condition, ExpressionNode trueValue, ExpressionNode falseValue) {
        return new IfExpressionNode(condition, trueValue, falseValue, implicitConversion);
    }

    public ExpressionNode negationNode(ExpressionNode node) {
        return new NegationOperatorExpressionNode(node, implicitConversion);
    }

    public ExpressionNode valueNode(Object value) {
        return new ValueExpressionNode(value);
    }

    public ExpressionNode fieldAccess(ExpressionNode subject, ExpressionNode fieldName) {
        return new FieldAccessExpressionNode(subject, fieldName);
    }

    public ExpressionNode mapAccess(ExpressionNode subject, ExpressionNode fieldName) {
        return new MapAccessExpressionNode(subject, fieldName);
    }

    public ExpressionNode functionCallNode(ExpressionNode pop, ArgumentsListExpressionNode functionArguments) {
        return FunctionCallExpressionNode.create(pop, functionArguments);
    }

    public ExpressionNode functionCallNode(ExpressionNode pop) {
        return FunctionCallExpressionNode.create(pop);
    }

    public ExpressionNode methodCall(ExpressionNode subject, ExpressionNode methodName, ArgumentsListExpressionNode functionArguments) {
        return MethodCallExpressionNode.create(subject, methodName, functionArguments, implicitConversion, methodExecutionFilter);
    }

    public ExpressionNode methodCall(ExpressionNode subject, ExpressionNode methodName) {
        return MethodCallExpressionNode.create(subject, methodName, implicitConversion, methodExecutionFilter);
    }

    public ArgumentsListExpressionNode argumentsList(ExpressionNode head, ArgumentsListExpressionNode tail) {
        return new ArgumentsListExpressionNode(head, tail);
    }

    public ArgumentsListExpressionNode argumentsList(ExpressionNode head) {
        return new ArgumentsListExpressionNode(head);
    }

    public IdentifierExpressionNode identifierNode(String identifier) {
        return new IdentifierExpressionNode(identifier);
    }

    public ExpressionNode variableNode(ExpressionNode variable) {
        return VariableExpressionNode.create(variable);
    }

}
