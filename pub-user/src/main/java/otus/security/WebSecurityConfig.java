package otus.security;

//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {//extends WebSecurityConfigurerAdapter {

//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http.authorizeHttpRequests(request -> request.anyRequest()
//                        .authenticated())
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }


//    @Autowired
//    UserService userService;

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.build();
//////                .csrf()
////                .disable()
////                .authorizeRequests()
////                //Доступ только для не зарегистрированных пользователей
////                .antMatchers("/registration").not().fullyAuthenticated()
////                //Доступ только для пользователей с ролью Администратор
////                .antMatchers("/admin/**").hasRole("ADMIN")
////                .antMatchers("/news").hasRole("USER")
////                //Доступ разрешен всем пользователей
////                .antMatchers("/", "/resources/**").permitAll()
////                //Все остальные страницы требуют аутентификации
////                .anyRequest().authenticated()
////                .and()
////                //Настройка для входа в систему
////                .formLogin()
////                .loginPage("/login")
////                //Перенарпавление на главную страницу после успешного входа
////                .defaultSuccessUrl("/")
////                .permitAll()
////                .and()
////                .logout()
////                .permitAll()
////                .logoutSuccessUrl("/");
//    }
//
//    @Autowired
//    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
//    }
}