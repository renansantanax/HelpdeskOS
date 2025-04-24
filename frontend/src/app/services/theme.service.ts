import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private renderer: Renderer2;
  private darkClass = 'dark-theme';
  private lightClass = 'light-theme';

  constructor(rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  enableDarkMode(): void {
    this.renderer.removeClass(document.body, this.lightClass);
    this.renderer.addClass(document.body, this.darkClass);
    localStorage.setItem('theme', 'dark');
  }

  enableLightMode(): void {
    this.renderer.removeClass(document.body, this.darkClass);
    this.renderer.addClass(document.body, this.lightClass);
    localStorage.setItem('theme', 'light');
  }

  loadTheme(): void {
    const theme = localStorage.getItem('theme') || 'light';
    theme === 'dark' ? this.enableDarkMode() : this.enableLightMode();
  }

  toggleTheme(): void {
    const current = localStorage.getItem('theme');
    current === 'dark' ? this.enableLightMode() : this.enableDarkMode();
  }

  isDark(): boolean {
    return localStorage.getItem('theme') === 'dark';
  }
}
