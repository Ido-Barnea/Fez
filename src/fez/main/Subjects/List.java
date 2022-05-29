package fez.main.Subjects;

import java.util.ArrayList;
import fez.main.Exceptions.IndexOutOfBoundException;
import fez.main.Exceptions.RuntimeException;
import fez.main.Objects.Position;
import fez.main.Objects.ResultObjects.InterpreterResult;

public class List extends Subject {
    
    public final ArrayList<Subject> elements;

    public List(ArrayList<Subject> elements) {
        super();
        this.elements = elements;
    }

    @Override
    public InterpreterResult add(Subject other, Position position) {
        List newList = copy();
        newList.elements.add(other);
        return new InterpreterResult(newList);
    }

    @Override
    public InterpreterResult retrieve(Subject other, Position position) {
        if (other instanceof List indexList) {
            try {
                if (indexList.size() > 1) { // If multiple values are requested
                    ArrayList<Subject> results = new ArrayList<>();
                    for (int i = 0; i < indexList.size(); i++) { // Add results to a new arrayList
                        results.add(elements.get(indexList.get(i).intValue()));
                    }
                    
                    return new InterpreterResult(new List(results)); // return new list with multiple values
                } else { // If only one value is requested
                    int index = indexList.get(0).intValue();
                    return new InterpreterResult(elements.get(index));
                }
            } catch (IndexOutOfBoundsException e) {
                return new InterpreterResult(
                        new IndexOutOfBoundException(
                                context,
                                position,
                                java.lang.String.format("Index %s is out of bound for size %s", other.intValue(), elements.size())
                        )
                );
            }
        }

        return illegalOperation(position);
    }

    public InterpreterResult updateItem(List indexes, Subject item) {
        for (Subject subject : indexes.elements) {
            int index = subject.intValue();
            if (index == -1) return new InterpreterResult(new RuntimeException(context, position, "Item not found."));
            else elements.set(index, item);
        }

        return new InterpreterResult(this);
    }

    public int size() {
        return elements.size();
    }

    public Subject get(int index) {
        return elements.get(index);
    }

    public List copy() {
        List newList = new List(elements);
        newList.setContext(context);
        newList.setPosition(position);
        return newList;
    }

    @Override
    public java.lang.String toString() {
        return elements.toString();
    }

}
