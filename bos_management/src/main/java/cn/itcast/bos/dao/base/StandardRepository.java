package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;


//在这里直接继承一个springDataJpa的接口,dao默认可以不加任何注解
public interface StandardRepository extends JpaRepository<Standard, Integer> {
	//查询方式一，按规则查询
	// 根据收派标准名称查询,这是一个测试等值查询的方法
	public List<Standard> findByName(String name);
	//实现一个模糊查询的方法
   public List<Standard> findByNameLike(String name);	
	
	//查询方式2，不符合命名规则的查询，查询语句写在方法上面
   @Query(value="from Standard where name=?",nativeQuery=false)
   public List<Standard>  queryname1(String name);
	//查询方式3，查询语句写在实体类的上面
   @Query(value="from Standard where name=?",nativeQuery=false)
   public List<Standard>  queryname2(String name);
	//d带参数的，修改的操作
	@Query(value="update Standard set minLength=?2 where id=?1") //这个用来确定参数的位置
	@Modifying
	public void updateMinSize(Integer id,Integer minLength);
	
   
}
