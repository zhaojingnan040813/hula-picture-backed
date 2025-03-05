package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Space;
import generator.service.SpaceService;
import generator.mapper.SpaceMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【space(空间)】的数据库操作Service实现
* @createDate 2025-03-05 16:40:18
*/
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
    implements SpaceService{

}




