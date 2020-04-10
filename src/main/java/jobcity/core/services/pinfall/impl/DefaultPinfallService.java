package jobcity.core.services.pinfall.impl;

import jobcity.BowlingApplication;
import jobcity.core.entities.Frame;
import jobcity.core.entities.Pinfall;
import jobcity.core.exceptions.BowlingApplicationException;
import jobcity.core.repositories.PinfallRepository;
import jobcity.core.services.pinfall.PinfallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DefaultPinfallService implements PinfallService {

    @Autowired
    private PinfallRepository pinfallRepository;

    @Override
    public List<Pinfall> findByFrame(final Frame frame) {
        return pinfallRepository.findByFrame(frame);
    }

    @Override
    public void save(final Frame frame, final int pinfalls) {
        List<Pinfall> pinfallList = findByFrame(frame);
        int currentFramePinfalls = 0;
        if (Objects.nonNull(pinfallList) && pinfallList.size() > 0) {
            currentFramePinfalls = pinfallList.get(0).getQuantity();
        }

        if (currentFramePinfalls + pinfalls > 10) {
            throw new BowlingApplicationException("The frame number " + frame.getFrameNumber() + " of the player '" + frame.getPlayer().getName() + "' can't has more than 10 pinfalls");
        }

        final Pinfall pinfall = new Pinfall();
        pinfall.setQuantity(pinfalls);
        pinfall.setFrame(frame);
        pinfallRepository.save(pinfall);
    }
}
