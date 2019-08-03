package similarwordsgenerator.model;

import org.junit.jupiter.api.BeforeEach;

class GeneratorTest {

    ProgramParameters.Builder parametersBuilder;
    ProgramParameters parameters;

    @BeforeEach
    void setParameters() {
        parametersBuilder = new ProgramParameters.Builder();
    }

/*    @Test
    void shouldThrowNullPointerExceptionWhenInputAndPathAreEmpty() {
        parameters = parametersBuilder.build();
        assertThrows(NullPointerException.class, () -> new Generator().generate(parameters));
    }*/


}
