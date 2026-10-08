package gui;

import config.SimulationConfiguration;
import engine.item.device.Light;
import engine.item.device.Switch;
import engine.item.furniture.Bathtub;
import engine.item.furniture.Bed;
import engine.item.furniture.Decor;
import engine.item.furniture.Furniture;
import engine.item.furniture.Oven;
import engine.item.furniture.Sofa;
import engine.item.furniture.Television;
import engine.item.furniture.WaterBottle;
import engine.map.BathRoom;
import engine.map.BedRoom;
import engine.map.Block;
import engine.map.Kitchen;
import engine.map.LivingRoom;
import engine.map.Map;
import engine.map.Room;
import engine.mobile.Friend;
import engine.mobile.Master;
import process.furniture.FurnitureManager;
import process.room.RoomManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * dessine la carte et les sprites.
 */
public class PaintStrategy {
	private static final Color DARK_ROOM= new Color(0, 0, 0, 100);
	private static final Color COLOR_BATHROOM = new Color(200, 220, 235);
	private static final Color COLOR_LIVINGROOM = new Color(210, 175, 130);
	private static final Color COLOR_KITCHEN = new Color(230, 215, 185);
	private static final Color COLOR_CORRIDOR = new Color(222, 195, 155);
	private static final Color COLOR_WALL = new Color(80, 50, 35);
	private static final Color COLOR_DOOR = new Color(140, 90, 50);
	private static final Color COLOR_EXIT = new Color(60, 160, 60);

	private static final Color COLOR_BEDROOM = new Color(205, 155, 105);
	private static final Color COLOR_WATER = new Color(90, 180, 200);
	private static final Color COLOR_BED = new Color(160, 110, 70);
	private static final Color COLOR_BATHTUB = new Color(80, 160, 210);
	private static final Color COLOR_SOFA = new Color(80, 120, 170);
	private static final Color COLOR_TV_OFF = new Color(25, 25, 25);
	private static final Color COLOR_TV_ON = new Color(80, 200, 255);
	private static final Color COLOR_OVEN = new Color(180, 70, 30);

	private static final Font FONT_FURNITURE = new Font(Font.SANS_SERIF, Font.BOLD, 10);

	private final SpritePanel masterBody = new SpritePanel("/sprites/MasterClassic.png");
	private final SpritePanel masterHair = new SpritePanel("/sprites/MasterHair.png");
	private final SpritePanel masterOutfit = new SpritePanel("/sprites/MasterOutfit.png");
	private final SpritePanel shadow = new SpritePanel("/sprites/Shadow.png");
	private final SpritePanel friendSprite = new SpritePanel("/sprites/Friend.png");


	private final SpritePanel bed = new SpritePanel("/sprites/beds_BR.png");
	private final SpritePanel kitchen = new SpritePanel("/sprites/kitchen_LRK.png");
	private final SpritePanel livingroom = new SpritePanel("/sprites/livingroom_LRK.png");
	private final SpritePanel bathroom = new SpritePanel("/sprites/fixtures_BA.png");
	private final SpritePanel sheetFloors  = new SpritePanel("/sprites/floorswalls_LRK.png");
	private final SpritePanel cabinets = new SpritePanel("/sprites/cabinets_BA.png");
	private final SpritePanel cabinet = new SpritePanel("/sprites/cabinets_LRK.png");
	private final SpritePanel newFloor = new SpritePanel ("/sprites/retro_bathroom_room_door_tiles1.png");
	private final SpritePanel wardrobes = new SpritePanel("/sprites/wardrobes_BR.png");
	private final SpritePanel bathretro = new SpritePanel("/sprites/retro_bathroom_set.png");
	private final SpritePanel deco  = new SpritePanel("/sprites/decorations_LRK.png");
	private final SpritePanel decoration = new SpritePanel("/sprites/decorations_BR.png");
	private final SpritePanel interior  = new SpritePanel("/sprites/Interior.png");

	//sprites chambre
	private BufferedImage spriteLit ;
	private BufferedImage spriteWardrobe;
	private BufferedImage spriteDresser;
	private BufferedImage spriteDresserchair;
	private BufferedImage tabledechevetL;
	private BufferedImage tabledechevetR;
	private BufferedImage spriteFloorLamp;

	private BufferedImage spritecadre;

	private BufferedImage spritelampsalon;
	private BufferedImage spriteBaignoire;
	private BufferedImage spriteBaignoireOpen;
	private BufferedImage spritetoilet;
	private BufferedImage spriteLavabo;
	private BufferedImage spriteLavaboOpen;

	private BufferedImage spriteCanape ;
	private BufferedImage spriteTV;

	private BufferedImage spriteFour ;
	private BufferedImage spriteFourOn;
	private BufferedImage spriteFridge;
	private BufferedImage spriteFridgeOpen;



	private BufferedImage spriteCabinet;

	private BufferedImage spritemeubletele;

	private BufferedImage spriteBookshelf;
	private BufferedImage spriteCoffeeTable;

	private BufferedImage spriteRug;
	private BufferedImage spritemeubleCuisine;
	private BufferedImage spritemeubleCuisine2;
	private BufferedImage spritemeubleCuisine3;
	private BufferedImage spritetablecuisine;


	private BufferedImage floorKitchen;
	private BufferedImage floorLivingroom;
	private BufferedImage floorBathroom;
	private BufferedImage floorBedroom;
	private BufferedImage spriteWall;
	private BufferedImage spritetabouret1;
	private BufferedImage spritecanap2 ;



	private Block lastMasterPosition = null;
	private int direction = SimulationConfiguration.DIRECTION_DOWN;
	private boolean isMoving = false;
	private int animationFrame = 0;

	private float fromPixelX = -1;
	private float fromPixelY = -1;
	private float toPixelX = -1;
	private float toPixelY = -1;
	private long moveStartTimeMs = 0L;
	private long moveDurationMs = SimulationConfiguration.GAME_SPEED;
	private long lastMoveDetectedMs = 0L;
	private long lastAnimationTickMs = 0L;


	public PaintStrategy() {

		spriteLit = bed.getSubImage(64,104,48,56);
		spriteBaignoire = bathroom.getSubImage(16,16,66,33);
		spriteCanape = livingroom.getSubImage(15,29,52,37);

		spritecanap2 = livingroom.getSubImage(336,34,32,31);


		spriteTV = livingroom.getSubImage(312,350,32,6);
		spriteFour = kitchen.getSubImage(193,209,30,40);
		spriteLavabo = bathroom.getSubImage(105, 13, 32, 20);
		spriteFridge  = kitchen.getSubImage(356, 24, 24, 56);
		spriteFridgeOpen = kitchen.getSubImage(356, 104, 28, 56);
		spriteBaignoireOpen = bathroom.getSubImage(15, 64, 65, 32);
		spriteLavaboOpen = bathroom.getSubImage(105, 60, 32, 22);
		spriteFourOn = kitchen.getSubImage(240, 208, 32, 42);
		spritemeubleCuisine  = kitchen.getSubImage(16, 204, 121, 45);
		spritemeubleCuisine2  = kitchen.getSubImage(144, 209, 32, 40);
		spritemeubleCuisine3  = kitchen.getSubImage(193, 138, 126, 22);
		spritetablecuisine  = interior.getSubImage(38, 263, 64, 56);
		spritetabouret1  = interior.getSubImage(17, 285, 20, 20);


		spritetoilet = bathroom.getSubImage(166, 16, 20, 44);


		floorBedroom  = sheetFloors.getSubImage(17,80,63,48);
		floorBathroom = newFloor.getSubImage(224,256,32,32);
		floorLivingroom = sheetFloors.getSubImage(146,86,62,41);
		floorKitchen = sheetFloors.getSubImage(146, 86, 62, 41);
		spriteWall = sheetFloors.getSubImage(146,49,61,32);





		spriteWardrobe   = wardrobes.getSubImage(16,  32, 48, 64); // armoire
		spriteDresser    = wardrobes.getSubImage(111,304, 35, 45); // commode
		spriteDresserchair    = wardrobes.getSubImage(106,274, 12, 10); // commode chair
		tabledechevetL    = wardrobes.getSubImage(161,213, 14, 19); // table de chevet
		tabledechevetR    = wardrobes.getSubImage(161,213, 14, 19);
		spritecadre    = deco.getSubImage(112,96, 32, 16);

		spriteCabinet    = bathretro.getSubImage(258,291, 28, 25); // armoire sdb  (1x2)


		spriteBookshelf  = cabinet.getSubImage(16,  80, 48, 48); // placard haut (1x2)

		spriteFloorLamp  = deco.getSubImage(120, 22, 16, 25); // lampe

		spriteRug        = livingroom.getSubImage(0,   256, 128, 32); // tapis beige
		spritemeubletele  = cabinet.getSubImage(592, 352, 48, 25);
		spriteCoffeeTable = livingroom.getSubImage(568,  182, 32, 16);
	}




	/**
	 * dessine la carte.
	 */
	public void paintMap(Map map, RoomManager roomManager, Graphics graphics) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		Room[] rooms = roomManager.getRooms();

		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = map.getBlock(lineIndex, columnIndex);
				int x = columnIndex * blockSize;
				int y = lineIndex * blockSize;

				Color fill;
				if (roomManager.isExit(block)) {
					fill = COLOR_EXIT;
				} else if (map.isOnBorder(block)) {
					fill = COLOR_WALL;
				} else if (roomManager.isWall(block)) {
					fill = COLOR_WALL;
				} else if (roomManager.isDoor(block)) {
					fill = COLOR_DOOR;
				} else if (roomManager.isCorridor(block)) {
					fill = COLOR_CORRIDOR;
				} else {
					fill = COLOR_CORRIDOR;
					for (Room room : rooms) {
						if (room.isRoom(block)) {
							fill = floorColor(room);
							break;
						}
					}
				}

				graphics.setColor(fill);
				graphics.fillRect(x, y, blockSize, blockSize);
			}
		}
	}



	private Color floorColor(Room room) {
		if (room instanceof BedRoom) {
			return COLOR_BEDROOM;
		}
		if (room instanceof BathRoom) {
			return COLOR_BATHROOM;
		}
		if (room instanceof LivingRoom) {
			return COLOR_LIVINGROOM;
		}
		if (room instanceof Kitchen) {
			return COLOR_KITCHEN;
		}
		return COLOR_CORRIDOR;
	}

	public void paintFloors(Map map, RoomManager roomManager, Graphics graphics) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;

		// dessiner chaque pièce d'un seul tenant
		paintRoomFloor(roomManager.getBedroom(),    floorBedroom,   graphics, blockSize);
		paintRoomFloor(roomManager.getBathroom(),   floorBathroom,  graphics, blockSize);
		paintRoomFloor(roomManager.getLivingroom(), floorLivingroom,graphics, blockSize);
		paintRoomFloor(roomManager.getKitchen(),    floorKitchen,   graphics, blockSize);

		// couloir : couleur unie
		for (int line = 0; line < map.getLineCount(); line++) {
			for (int col = 0; col < map.getColumnCount(); col++) {
				Block block = map.getBlock(line, col);
				if (roomManager.isCorridor(block)
						&& !map.isOnBorder(block)
						&& !roomManager.isWall(block)) {
					graphics.setColor(COLOR_CORRIDOR);
					graphics.fillRect(col * blockSize, line * blockSize,
							blockSize, blockSize);
				}
			}
		}

		// Tapis dessinés après le sol, avant les meubles
		if (spriteRug != null) {
			// Salon : 4 blocs de large, 1 bloc de haut, à partir de (ligne 14, col 2)
			graphics.drawImage(spriteRug, 2 * blockSize, 14 * blockSize, 4 * blockSize, blockSize, null);
		}
	}


	/**
	 * dessine la texture sur toute la surface d'une pièce d'un seul appel.
	 * Évite les lignes de jointure entre les tuiles.
	 */
	private void paintRoomFloor(Room room, BufferedImage floor,
	                            Graphics graphics, int blockSize) {
		if (room == null || floor == null) return;

		int x = room.getStartColumn() * blockSize;
		int y = room.getStartLine()   * blockSize;
		int w = (room.getEndColumn() - room.getStartColumn() + 1) * blockSize;
		int h = (room.getEndLine()   - room.getStartLine()   + 1) * blockSize;

		graphics.drawImage(floor, x, y, w, h, null);
	}

	public void paintWalls(Map map, RoomManager roomManager, Graphics graphics) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;

		for (int line = 0; line < map.getLineCount(); line++) {
			for (int col = 0; col < map.getColumnCount(); col++) {
				Block block = map.getBlock(line, col);
				int x = col  * blockSize;
				int y = line * blockSize;

				if (roomManager.isExit(block)) {
					graphics.setColor(COLOR_EXIT);
					graphics.fillRect(x, y, blockSize, blockSize);

				} else if (map.isOnBorder(block) || roomManager.isWall(block)) {
					if (spriteWall != null) {
						graphics.drawImage(spriteWall, x, y,
								blockSize, blockSize, null);
					} else {
						graphics.setColor(COLOR_WALL);
						graphics.fillRect(x, y, blockSize, blockSize);
					}

				} else if (roomManager.isDoor(block)) {
					// porte → couleur unie uniquement
					graphics.setColor(COLOR_DOOR);
					graphics.fillRect(x, y, blockSize, blockSize);
				}
			}
		}
	}

	/**
	 * dessine les meubles.
	 */
	public void paintFurnitures(FurnitureManager furnitureManager, Graphics graphics) {
		drawFurniture(furnitureManager.getBed(),spriteLit,graphics);
		BufferedImage bathtubSprite = furnitureManager.getBathtub().isActive() ? spriteBaignoireOpen:spriteBaignoire;
		drawFurniture(furnitureManager.getBathtub(),bathtubSprite,graphics);
		drawFurniture(furnitureManager.getSofa(),spriteCanape,graphics);
		BufferedImage ovenSprite = furnitureManager.getOven().isPreheating() ? spriteFourOn:spriteFour;
		drawFurniture(furnitureManager.getOven(),ovenSprite,graphics);
		drawFurniture(furnitureManager.getTelevision(),spriteTV,graphics);
		BufferedImage fridgeSprite = furnitureManager.getWaterBottle().isActive() ? spriteFridgeOpen:spriteFridge;
		drawFurniture(furnitureManager.getWaterBottle(),fridgeSprite,graphics);
		BufferedImage sinkSprite = furnitureManager.getSink().isActive() ? spriteLavaboOpen:spriteLavabo;
		drawFurniture(furnitureManager.getSink(),sinkSprite,graphics);

		for (Decor decor : furnitureManager.getDecors()) {
			drawFurniture(decor, getSpriteForDecor(decor.getName()), graphics);
		}
	}

	private void drawFurniture(Furniture furniture, BufferedImage sprite,
	                           Graphics graphics) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		int minCol  = Integer.MAX_VALUE, minLine = Integer.MAX_VALUE;
		int maxCol  = Integer.MIN_VALUE, maxLine = Integer.MIN_VALUE;
		for (Block block : furniture.getBlocks()) {
			minCol  = Math.min(minCol,  block.getColumn());
			minLine = Math.min(minLine, block.getLine());
			maxCol  = Math.max(maxCol,  block.getColumn());
			maxLine = Math.max(maxLine, block.getLine());
		}
		int x = minCol  * blockSize;
		int y = minLine * blockSize;
		int w = (maxCol  - minCol  + 1) * blockSize;
		int h = (maxLine - minLine + 1) * blockSize;
		if (sprite != null) {
			graphics.drawImage(sprite, x, y, w, h, null);
		} else {
			graphics.setColor(furnitureColor(furniture));
			graphics.fillRect(x + 2, y + 2, w - 4, h - 4);
			graphics.setColor(Color.WHITE);
			graphics.setFont(FONT_FURNITURE);
			graphics.drawString(furniture.getName(), x + 3, y + 12);
		}
	}

	private Color furnitureColor(Furniture furniture) {
		if (furniture instanceof Bed) {
			return COLOR_BED;
		}
		if (furniture instanceof Bathtub) {
			return COLOR_BATHTUB;
		}
		if (furniture instanceof Sofa) {
			return COLOR_SOFA;
		}
		if (furniture instanceof Oven) {
			return COLOR_OVEN;
		}
		if (furniture instanceof WaterBottle) {
			return COLOR_WATER;
		}
		if (furniture instanceof Television) {
			Television tv = (Television) furniture;
			return tv.isActive() ? COLOR_TV_ON : COLOR_TV_OFF;
		}
		return Color.GRAY;
	}

	private BufferedImage getSpriteForDecor(String name) {
		switch (name) {
			case "Wardrobe": return spriteWardrobe;
			case "Dresser": return spriteDresser;
			case "Cabinet": return spriteCabinet;
			case "Bookshelf": return spriteBookshelf;
			case "Lamp": return spriteFloorLamp;

			case "CoffeeTable": return spriteCoffeeTable;
			case "Fridge": return spriteFridge;
			case "tabledechevetgauche": return tabledechevetL;
			case "tabledechevetdroit": return tabledechevetR;
			case "Toilet": return spritetoilet;
			case "Lamp2" : return spriteFloorLamp;
			case "meubleCuisine" : return spritemeubleCuisine;
			case "meubleCuisine2" : return spritemeubleCuisine2;
			case "meubleCuisine3" : return spritemeubleCuisine3;
			case "tabledecuisine" : return spritetablecuisine;
			case "tabouretdroit" : return spritetabouret1;
			case "tabouretgauche" : return spritetabouret1;
			case "cadre": return spritecadre;
			case "Dresserchair": return spriteDresserchair;
			case "meubletele" : return spritemeubletele;
			case "canape" : return spritecanap2;

			default: return null; // rectangle gris avec le nom
		}
	}

	private void updateDirection(Master master) {
		Block currentPosition = master.getPosition();
		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		long now = System.currentTimeMillis();

		if (lastMasterPosition == null) {
			lastMasterPosition = currentPosition;
			fromPixelX = toPixelX = currentPosition.getColumn() * blockSize;
			fromPixelY = toPixelY = currentPosition.getLine()   * blockSize;
			lastMoveDetectedMs = now;
			lastAnimationTickMs = now;
			isMoving = false;
			return;
		}

		int currentLine = currentPosition.getLine();
		int currentCol  = currentPosition.getColumn();
		int lastLine = lastMasterPosition.getLine();
		int lastCol  = lastMasterPosition.getColumn();

		boolean blockChanged = (currentLine != lastLine) || (currentCol != lastCol);

		if (blockChanged) {
			if (currentCol > lastCol) {
				direction = SimulationConfiguration.DIRECTION_RIGHT;
			} else if (currentCol < lastCol) {
				direction = SimulationConfiguration.DIRECTION_LEFT;
			} else if (currentLine > lastLine) {
				direction = SimulationConfiguration.DIRECTION_DOWN;
			} else {
				direction = SimulationConfiguration.DIRECTION_UP;
			}

			// durée d'interpolation = temps réel écoulé depuis le dernier changement de bloc,
			// bornée pour éviter des slides absurdes après une longue immobilité.
			long elapsed = now - lastMoveDetectedMs;
			moveDurationMs = Math.max(50L, Math.min(elapsed, 1000L));

			// la nouvelle anim part de la position actuellement rendue (continuité visuelle).
			fromPixelX = toPixelX;
			fromPixelY = toPixelY;
			toPixelX = currentCol  * blockSize;
			toPixelY = currentLine * blockSize;
			moveStartTimeMs = now;
			lastMoveDetectedMs = now;

			lastMasterPosition = currentPosition;
		}

		// le maître est considéré "en mouvement" tant que l'interpolation n'est pas finie.
		isMoving = (now - moveStartTimeMs) < moveDurationMs;

		if (isMoving) {
			if (now - lastAnimationTickMs >= SimulationConfiguration.WALK_ANIMATION_FRAME_MS) {
				animationFrame++;
				if (animationFrame < 1 || animationFrame > 5) {
					animationFrame = 1;
				}
				lastAnimationTickMs = now;
			}
		} else {
			animationFrame = 0;
		}
	}

	private BufferedImage getFrame(BufferedImage sheet, int col, int row) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		return sheet.getSubimage(col * blockSize, row * blockSize, blockSize, blockSize);
	}

	private int getDirection() {
		return directionFrame(direction);
	}

	private int directionFrame(int directionValue) {
		if (directionValue == SimulationConfiguration.DIRECTION_UP) {
			return 12;
		}
		if (directionValue == SimulationConfiguration.DIRECTION_LEFT) {
			return 18;
		}
		if (directionValue == SimulationConfiguration.DIRECTION_RIGHT) {
			return 6;
		}
		return 0;
	}

	/**
	 * dessine le maître.
	 */
	public void paint(Master master, Furniture currentFurniture, Graphics graphics) {
		updateDirection(master);
		int blockSize = SimulationConfiguration.BLOCK_SIZE;

		long now = System.currentTimeMillis();
		float ratio = moveDurationMs <= 0 ? 1f
				: Math.min(1f, (now - moveStartTimeMs) / (float) moveDurationMs);
		float interpX = fromPixelX + (toPixelX - fromPixelX) * ratio;
		float interpY = fromPixelY + (toPixelY - fromPixelY) * ratio;
		int x = Math.round(interpX);
		int y = Math.round(interpY);

		int forcedDirection = direction;
		if (currentFurniture instanceof Bed) {
			Block target = currentFurniture.getBlocks().get(0);
			x = target.getColumn() * blockSize;
			y = target.getLine() * blockSize;
			forcedDirection = SimulationConfiguration.DIRECTION_UP;
		} else if (currentFurniture instanceof Sofa || currentFurniture instanceof Television) {
			Block target = currentFurniture.getBlocks().get(0);
			x = target.getColumn() * blockSize;
			y = (target.getLine() + 1) * blockSize;
			forcedDirection = SimulationConfiguration.DIRECTION_DOWN;
		} else if (currentFurniture instanceof Oven) {
			// Le maitre mange : assis sur le tabouret de gauche, regarde a droite.
			x = 25 * blockSize;
			y = 17 * blockSize;
			forcedDirection = SimulationConfiguration.DIRECTION_RIGHT;
		} else if (currentFurniture instanceof Bathtub) {
			// Centre-droit de la baignoire, sur la zone bleue.
			x = 22 * blockSize + blockSize / 2;
			y = 2 * blockSize + blockSize / 2;
			forcedDirection = SimulationConfiguration.DIRECTION_DOWN;
		}

		int currentFrame = directionFrame(forcedDirection) + animationFrame;
		BufferedImage spriteBody = getFrame(masterBody.getImage(), currentFrame, 0);
		BufferedImage spriteOutfit = getFrame(masterOutfit.getImage(), currentFrame, 0);
		BufferedImage spriteHair = getFrame(masterHair.getImage(), currentFrame, 5);
		BufferedImage spriteShadow = getFrame(shadow.getImage(), 0, 0);

		graphics.drawImage(spriteShadow, x, y, blockSize, blockSize, null);
		graphics.drawImage(spriteBody, x, y, blockSize, blockSize, null);
		graphics.drawImage(spriteOutfit, x, y, blockSize, blockSize, null);
		graphics.drawImage(spriteHair, x, y, blockSize, blockSize, null);
	}

	/**
	 * dessine l'ami assis sur le canape pendant l'imprevu.
	 */
	public void paint(Friend friend, Graphics graphics) {
		if (friend == null) {
			return;
		}

		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		Block position = friend.getPosition();
		int drawX = position.getColumn() * blockSize;
		int drawY = position.getLine() * blockSize;
		int drawW = blockSize;
		int drawH = blockSize;

		BufferedImage sprite = friendSprite.getSubImage(48, 203, 16, 21);
		if (sprite != null) {
			graphics.drawImage(sprite, drawX, drawY, drawW, drawH, null);
		} else {
			graphics.setColor(new Color(220, 110, 60));
			graphics.fillRect(drawX, drawY, drawW, drawH);
		}
	}


	public void paintDarkRoom(Map map, RoomManager roomManager, Graphics graphics) {
		int blockSize = SimulationConfiguration.BLOCK_SIZE;
		Room[] rooms = roomManager.getRooms();

		for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
			for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
				Block block = map.getBlock(lineIndex, columnIndex);
				int x = columnIndex * blockSize;
				int y = lineIndex * blockSize;
				for(Room room: rooms) {
					if (room.isRoom(block)) {
						if (!room.getLight().isOn()){
							graphics.setColor(DARK_ROOM);
							graphics.fillRect(x, y, blockSize, blockSize);
						}
					}
				}
			}
		}
	}
}
