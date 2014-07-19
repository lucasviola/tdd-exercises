import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AnythingTest {

    @Test
    public void iteratorShouldReturnHelloWorld() throws Exception {
        //arrange
        Iterator iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("Hello").thenReturn("World");

        //act
        String result = iterator.next() + " " + iterator.next();

        //assert
        assertEquals(result, "Hello World");
    }

    @Test
    public void comparingArguments() throws Exception {
        Comparable comparable = mock(Comparable.class);
        //stub
        when(comparable.compareTo("Test")).thenReturn(1);
        when(comparable.compareTo("Hello World")).thenReturn(0);

        assertEquals(comparable.compareTo("Test"), 1);
        assertEquals(comparable.compareTo("Hello World"), 0);

    }

    @Test
    public void comparingWithUnspecifiedArguments() throws Exception {
        Comparable comparable = mock(Comparable.class);
        when(comparable.compareTo(anyInt())).thenReturn(-1);

        int result = comparable.compareTo(666);

        assertEquals(result, -1);
    }

    @Test(expected = IOException.class)
    public void OutputStreamWriterRethrowsAnExceptionFromOutputStream() throws IOException {
        OutputStream outputStreamMock = mock(OutputStream.class);
        OutputStreamWriter osw = new OutputStreamWriter(outputStreamMock);
        doThrow(new IOException()).when(outputStreamMock).close();

        osw.close();
    }

    @Test
    public void OutputStreamWriterClosesOutputStreamOnClose() throws IOException {
        OutputStream outputStreamMock = mock(OutputStream.class);
        OutputStreamWriter osw = new OutputStreamWriter(outputStreamMock);

        osw.close();

        verify(outputStreamMock).close();

    }


}
