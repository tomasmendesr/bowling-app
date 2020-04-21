package jobsity.core.services.pinfall.impl;

import jobsity.core.entities.Frame;
import jobsity.core.entities.Pinfall;
import jobsity.core.exceptions.BowlingApplicationException;
import jobsity.core.repositories.PinfallRepository;
import jobsity.core.services.pinfall.PinfallService;
import jobsity.core.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jobsity.core.services.frame.impl.DefaultFrameService.MAX_FRAME_NUMBER;

@Service
public class DefaultPinfallService implements PinfallService {

    private PinfallRepository pinfallRepository;

    @Autowired
    public DefaultPinfallService(PinfallRepository pinfallRepository){
        this.pinfallRepository = pinfallRepository;
    }

    @Override
    public List<Pinfall> findByFrame(final Frame frame) {
        if (Objects.isNull(frame.getId())) {
            return new ArrayList<>();
        }
        final List<Pinfall> pinfalls = pinfallRepository.findByFrame(frame);
        pinfalls.sort(Comparator.comparing(p -> p.getId()));
        return pinfalls;
    }

    @Override
    public Pinfall saveNewPinfall(final Frame frame, final int pinfallsQuantity) {
        final Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(pinfallsQuantity);
        pinfall.setFrame(frame);

        validateNewPinfallInFrame(frame, pinfall);
        return save(pinfall);
    }

    @Override
    public void validateNewPinfallInFrame(final Frame frame, final Pinfall newPinfall) {
        final List<Pinfall> pinfallList = findByFrame(frame);
        final int currentFramePinfalls = CollectionUtils.isNotEmpty(pinfallList) ? pinfallList.get(0).getQuantity() : 0;
        final int finalFramePinfalls = currentFramePinfalls + newPinfall.getQuantity();
        if ((finalFramePinfalls > 10 || finalFramePinfalls < 0) && frame.getFrameNumber() != MAX_FRAME_NUMBER) {
            throw new BowlingApplicationException("The pinfalls from frame number " + frame.getFrameNumber() + " of the player '" + frame.getPlayer().getName() + "' has to be between 0 and 10 has more than 10 pinfalls");
        }
    }

    @Override
    public int calculateQuantityFromFrame(final Frame frame) {
        return calculateQuantityFromFrame(frame, true);
    }

    @Override
    public int calculateQuantityFromFrame(final Frame frame, boolean calculateOnlyFirstAndSecondShoot) {
        List<Pinfall> pinfalls = findByFrame(frame);
        if (calculateOnlyFirstAndSecondShoot) {
            pinfalls = pinfalls.stream().limit(2).collect(Collectors.toList());
        }
        int result = pinfalls.stream().mapToInt(Pinfall::getQuantity).sum();
        return result;
    }

    @Override
    public boolean isAStrike(Frame frame) {
        final List<Pinfall> pinfalls = findByFrame(frame);
        return isAStrikeOrSpear(pinfalls.get(0).getQuantity());
    }

    @Override
    public int getFirstPinfallQuantityFromFrame(Frame frame) {
        List<Pinfall> pinfalls = findByFrame(frame);
        return pinfalls.isEmpty() ? 0 : pinfalls.get(0).getQuantity();
    }

    @Override
    public Pinfall save(final Pinfall pinfall) {
        return pinfallRepository.save(pinfall);
    }

    @Override
    public boolean isAStrikeOrSpear(int quantity) {
        return quantity == 10;
    }
}
