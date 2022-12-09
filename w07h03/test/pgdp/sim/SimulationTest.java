package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class SimulationTest {
    void runTest(String seed, int width, int height, String[] states) {
        runTest(seed.getBytes(StandardCharsets.UTF_8), width, height, states);
    }

    void runTest(byte[] seed, int width, int height, String[] states) {
        RandomGenerator.reseed(seed);
        var cells = FieldVisualizeHelper.fieldDescriptionToCellArray(states[0], width, height);
        var a = new Simulation(cells, width, height);
        for (int i = 1; i < states.length; i++) {
            a.tick();
            Assertions.assertEquals(states[i] + "\n", FieldVisualizeHelper.cellArrayToFieldDescription(a.getCells(), width, height, false), "State " + i + ", " + (i + 1) + ".ter String");
        }
    }

    @Test
    @DisplayName("One single plant")
    void singlePlant() {
        var states = new String[]{"""
                g . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                g g . . .
                g . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                g g g . .
                g g . . .
                g . . . .
                . . . . .
                . . . . .""", """
                g g g . .
                g g g . .
                g g . . .
                . . . . .
                . . . . .""", """
                g g g g .
                g g g g .
                g g g g .
                g g . . .
                . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 0;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("single pingu, should reproduce and create big pingu")
    void singlePingu() {
        var states = new String[]{"""
                p . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p . . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p p . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p p p . .
                p p . . .
                p . . . .
                . . . . .
                . . . . .""", """
                p p p p .
                p p p . .
                p p . . .
                . p . . .
                . . . . ."""};
        SimConfig.pinguInitialFood = 3;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 1;
        runTest("plant", 5, 5, states);
    }

    @Test
    void pinguEatsPlant() {
        var states = new String[]{"""
                p g . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p . . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . ."""};
        SimConfig.pinguInitialFood = 1;
        SimConfig.pinguConsumedFood = 2;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 2;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("hamster eats plant, reproduces, wolf eats hamster child, reproduces")
    void hamsterEatsPlantWolfEatsHamster() {
        var states = new String[]{"""
                h g . . .
                . . . . .
                w . . . .
                . . . . .
                . . . . .""", """
                h . . . .
                h . . . .
                w . . . .
                . . . . .
                . . . . .""", """
                . . . . .
                . w . . .
                . . . . .
                w . . . .
                . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 1;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        runTest("plant", 5, 5, states);
    }
}