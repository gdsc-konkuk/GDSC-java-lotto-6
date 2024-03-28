package lotto.controller;

import lotto.model.Person;
import lotto.model.Shop;
import lotto.model.lotto.LottoResults;
import lotto.model.lotto.LottoWinNumbers;
import lotto.util.Calculator;
import lotto.util.Log;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {
    private final IOController ioController;
    private final Person person;
    private final Shop shop;
    private LottoResults lottoResults;
    private static final Log log = new Log();

    public LottoController() {
        this.ioController = new IOController(new InputView(), new OutputView());
        this.person = Person.of(ioController.purchaseAmountInput());
        this.shop = Shop.of(person.getPurchaseAmout());
    }

    public void process() {
        buyLotto();
        inputWinNumbers();
        showResults();
    }

    private void buyLotto() {
        ioController.lottoCountCheck(shop.getPurchaseNumber());
        person.buyLotto(shop.givePaperBag());
        ioController.showLotto(person.getLottoesToString());
    }

    private void inputWinNumbers() {
        try {
            LottoWinNumbers winNumbers =
                    LottoWinNumbers.of(ioController.winningNumberInput(), ioController.bonusNumberInput());
            this.lottoResults = LottoResults.of(winNumbers);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            inputWinNumbers();
        }
    }

    private void showResults() {
        lottoResults.calculateResult(person.getPaperBag());
        ioController.showResult(lottoResults.resultToString());
        ioController.showRevenue(calculateRevenue());
    }

    private double calculateRevenue() {
        int totalIncome = lottoResults.getTotalWinAmount();
        int purchasePrice = person.getPurchaseAmout();
        return Calculator.getRevenue(purchasePrice, totalIncome);
    }
}
