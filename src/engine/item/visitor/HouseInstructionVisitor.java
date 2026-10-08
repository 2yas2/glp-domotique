package engine.item.visitor;

import engine.item.visitor.AbstractFurnitureVisitor;
import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Furniture;
import engine.item.furniture.Oven;
import engine.item.furniture.Sink;
import engine.item.furniture.Sofa;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;

public class HouseInstructionVisitor extends AbstractFurnitureVisitor<String> {

    public HouseInstructionVisitor(Furniture furniture) {
        super("Le maitre doit utiliser " + furniture.getName());
    }

    @Override
    public String visit(Bed bed) {
        return "Le maitre doit dormir";
    }

    @Override
    public String visit(Bathtub bathtub) {
        return "Le maitre doit prendre une douche";
    }

    @Override
    public String visit(Oven oven) {
        return "Le maitre doit manger";
    }

    @Override
    public String visit(Sofa sofa) {
        return "Le maitre doit se detendre devant la tele";
    }

    @Override
    public String visit(Television television) {
        return "Le maitre doit regarder la tele";
    }

    @Override
    public String visit(WaterBottle waterBottle) {
        return "Le maitre doit boire";
    }

    @Override
    public String visit(Sink sink) {
        return "Le maitre doit se laver les mains";
    }
}