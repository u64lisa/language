package axiol.parser.tree;

public enum NodeType {

    // EXPRESSIONS
    MATCH_EXPR,
    ELEMENT_REFERENCE_EXPR,
    BOOLEAN_EXPR,
    NUMBER_EXPR,
    STRING_EXPR,
    ARRAY_EXPR,
    BINARY_EXPR,
    UNARY_EXPR,
    LITERAL_EXPR,
    CALL_EXPR,
    CAST_EXPR,

    // CONTROL FLOW
    BREAK_STATEMENT,
    CONTINUE_STATEMENT,
    DO_WHILE_STATEMENT,
    FOR_STATEMENT,
    IF_STATEMENT,
    LOOP_STATEMENT,
    RETURN_STATEMENT,
    SWITCH_STATEMENT,
    UNREACHABLE_STATEMENT,
    WHILE_STATEMENT,
    YIELD_STATEMENT,

    // OOP
    CLASS_TYPE_STATEMENT,
    CONSTRUCT_STATEMENT,
    FUNCTION_STATEMENT,
    STRUCT_TYPE_STATEMENT,
    UDT_DECLARE_STATEMENT,

    // SPECIAL
    NATIVE_STATEMENT,
    STACK_ALLOC,

    // EXTRA
    BODY_STATEMENT,
    VAR_STATEMENT,
    LINKED_STATEMENT,

    // ROOT
    ROOT,

    // place holder
    EMPTY
    ;
}
