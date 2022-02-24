package com.i3market.semanticengine;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

public class ControllerTest {

    @Test
    void test() {

        List<Integer> list = new ArrayList<>();

        var x = Flux.range(1, 5);

        x.subscribe(i -> list.add(i));

        System.out.println(list.size());
    }
}
