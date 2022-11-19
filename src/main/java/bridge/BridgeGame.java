package bridge;

import bridge.constant.Constant;
import bridge.domain.*;
import bridge.service.BridgeMaker;
import bridge.view.InputView;
import bridge.view.OutputView;

public class BridgeGame {
    private final BridgeMaker bridgeMaker;
    private Movement movement;
    private final BridgeMap bridgeMap = new BridgeMap();
    private int tryCount = Constant.INITIAL_COUNT;

    public BridgeGame(BridgeMaker bridgeMaker) {
        this.bridgeMaker = bridgeMaker;
    }

    public void startGame() {
        BridgeSize bridgeSize = InputView.readBridgeSize();
        Bridge bridge = new Bridge(bridgeMaker.makeBridge(bridgeSize.getSize()));
        movement = new Movement(bridge);
        repeatGame();
    }

    private void repeatGame() {
        boolean isContinue = true;
        do {
            move(InputView.readMoving());
            if (!movement.canMove()) {
                isContinue = retry();
            }
        } while (!movement.isFinish() && isContinue);
        OutputView.printResult(bridgeMap.getMap(), movement.isSuccess(), tryCount);
    }

    private void move(Moving moving) {
        movement.saveMoving(moving);
        saveCompareResult(moving);
        OutputView.printMap(bridgeMap.getMap());
    }

    private boolean retry() {
        Command command = InputView.readGameCommand();
        if (command.isRetry()) {
            tryCount++;
            clearGame();
            return true;
        }
        return false;
    }

    private void clearGame() {
        movement.clearMoving();
        bridgeMap.clearMap();
    }

    private void saveCompareResult(Moving moving) {
        bridgeMap.addMap(moving, movement.canMove());
    }
}