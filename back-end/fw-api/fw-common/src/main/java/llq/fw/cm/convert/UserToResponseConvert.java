package llq.fw.cm.convert;

import java.util.Objects;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.core.convert.converter.Converter;

import llq.fw.cm.models.User;
import llq.fw.cm.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserToResponseConvert implements Converter<User, UserResponse> {

    @Override
    public UserResponse convert(@Nullable User source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return UserResponse.builder().id(source.getId())
		.username(source.getUsername())
		.fullname(source.getFullname())
		.build();
    }
}