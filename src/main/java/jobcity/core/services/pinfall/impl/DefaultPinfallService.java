package jobcity.core.services.pinfall.impl;

import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.repositories.PinfallRepository;
import jobcity.core.services.pinfall.PinfallService;
import jobcity.core.utils.inputreadervalidator.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPinfallService implements PinfallService {

    @Autowired
    private PinfallRepository pinfallRepository;

    @Override
    public List<Pinfall> findByFrame(final Frame frame) {
        return pinfallRepository.findByFrame(frame);
    }

    @Override
    public void saveNewPinfall(final Frame frame, final int pinfalls) {
        final Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(pinfalls);
        pinfall.setFrame(frame);

        validateNewPinfallInFrame(frame, pinfall);
        save(pinfall);
    }

    @Override
    public void validateNewPinfallInFrame(final Frame frame, final Pinfall newPinfall) {
        final List<Pinfall> pinfallList = findByFrame(frame);
        final int currentFramePinfalls = CollectionUtils.isNotEmpty(pinfallList) ? pinfallList.get(0).getQuantity() : 0;
        final int finalFramePinfalls = currentFramePinfalls + newPinfall.getQuantity();
        if (finalFramePinfalls > 10 || finalFramePinfalls < 0) {
            throw new BowlingApplicationException("The pinfalls from frame number " + frame.getFrameNumber() + " of the player '" + frame.getPlayer().getName() + "' has to be between 0 and 10 has more than 10 pinfalls");
        }
    }

    @Override
    public void save(final Pinfall pinfall) {
        pinfallRepository.save(pinfall);
    }
}
