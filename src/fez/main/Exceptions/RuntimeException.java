package fez.main.Exceptions;

import fez.main.Objects.Context;
import fez.main.Objects.Position;

public class RuntimeException extends Exception {

    private final Context context;

    public RuntimeException(Context context, Position position, String message) {
        super(position, "Runtime Exception", message);
        this.context = context;
    }

    public RuntimeException(Context context, Position position, String name, String message) {
        super(position, name, message);
        this.context = context;
    }

    private String generateTraceback() {
        StringBuilder result = new StringBuilder();
        Context context_ = this.context;
        Position position_ = this.position;

        while (context_ != null) {
            result.append(String.format(
                    "  File %s, line %s, in %s\n",
                    position_.fileName(),
                    position_.line() + 1,
                    context_.displayName()
            ));

            position_ = context_.entryPosition();
            context_ = context_.parent();
        }

        return "Traceback: (most recent is first)\n" + result;
    }

    @Override
    public String toString() {
        String result = String.format("\n\n%s: %s", errorName, errorMessage);
        result += String.format("\n\n %s\n", stringWithArrowPointer(position));
        result += "\n" + generateTraceback();

        return result;
    }

}
